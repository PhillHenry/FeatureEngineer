package uk.co.odinconsultants.features.spark

import uk.co.odinconsultants.features.domain_ip.address.Tld2DnsParser
import org.apache.spark.sql.functions.{udf, col}
import uk.co.odinconsultants.features.domain_ip.address.DomainNameRegistry._
import uk.co.odinconsultants.features.domain_ip.address.CityLookup._
import uk.co.odinconsultants.features.domain_ip.address.CountryLookup._
import org.apache.spark.sql.DataFrame
import java.sql.Timestamp

object Enhancement {

  val NoDate: Timestamp = new Timestamp(Long.MaxValue)

  def toMaybeDate(dns: String, domain: String): Option[Timestamp] =
    apacheWhoIs(dns, domain).map(_._1).map(x => new Timestamp(x.getTime))

  def enhanceAndRegister(df: DataFrame, tlds: Set[String], table: String): DataFrame = {
    Tld2DnsParser.readMappings.right.map { mappings =>
      val t2d = sortByLongestTLD(mappings.toSeq)
      val orderedTLDs = longestToShortest(tlds)

      import scala.collection.convert.decorateAsScala._
      val x2Whois = new java.util.concurrent.ConcurrentHashMap[String, Timestamp]().asScala

      def toWhois(x: String): Timestamp = {
        x2Whois.getOrElse(x, {
          val domain  = clean(orderedTLDs, x)
          val whois   = suitableDNSFor(domain, t2d).flatMap(dns => toMaybeDate(dns, domain)).getOrElse(NoDate)
          x2Whois(x) = whois
          whois
        })
      }

      val toWhoisUDF  = udf(toWhois _)
      val latUDF      = udf(latitude _)
      val longUDF     = udf(longitude _)
      val countryUDF  = udf(lookupCountry _)

      val enhanced = df
        .withColumn("whois", toWhoisUDF(col("destinationHostName")))
        .withColumn("lat", latUDF(col("destinationAddress")))
        .withColumn("lon",  longUDF(col("destinationAddress")))
        .withColumn("country", countryUDF(col("destinationAddress")))
        .cache()
      enhanced.createOrReplaceTempView(table)
      enhanced
    }.right.get
  }

}
