import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

class Scraper {

  def main(args : Array[String]) = {

  }

  def download(url : String) : Document = {
    var doc : Document = null;
    doc = Jsoup.connect(url)
          .userAgent("Mozilla")
          .timeout(3000)
          .post();
    return doc;
  }
}
