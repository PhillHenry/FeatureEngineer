package uk.co.odinconsultants.features.spark

import uk.co.odinconsultants.features.domain_ip.address.Tld2DnsParser
import org.apache.spark.sql.functions.{udf, col}
import uk.co.odinconsultants.features.domain_ip.address.DomainNameRegistry._
import uk.co.odinconsultants.features.domain_ip.address.CityLookup._
import uk.co.odinconsultants.features.domain_ip.address.CountryLookup._
import org.apache.spark.sql.DataFrame

object Enhancement {

  def enhanceAndRegister(df: DataFrame, tlds: Set[String], table: String): DataFrame = {
    Tld2DnsParser.readMappings.right.map { mappings =>
      val t2d = sortByLongestTLD(mappings.toSeq)
      val orderedTLDs = longestToShortest(tlds)

      def toWhois(x: String): String = {
        val domain = clean(orderedTLDs, x)
        suitableDNSFor(domain, t2d).flatMap(dns => apacheWhoIs(dns, domain).map(_._1)).getOrElse("-").toString
      }

      val toWhoisUDF = udf(toWhois _)
      val latUDF = udf(latitude _)
      val longUDF = udf(longitude _)
      val countryUDF = udf(lookupCountry _)

      val enhanced = df
        .withColumn("whois", toWhoisUDF(col("destinationHostName")))
        .withColumn("lat", latUDF(col("destinationAddress")))
        .withColumn("lon", longUDF(col("destinationAddress")))
        .withColumn("country", countryUDF(col("destinationAddress")))
        .cache()
      enhanced.createOrReplaceTempView(table)
      enhanced
    }.right.get
  }

}
