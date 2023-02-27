import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
abstract class Link {
    private String url;

    public Link(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public abstract String getShortCode();
}


class ShortenedLink extends Link {
    private String shortCode;

    public ShortenedLink(String url) {
        super(url);
        shortCode = Base64.getEncoder().encodeToString(url.getBytes());
    }

    @Override
    public String getShortCode() {
        return shortCode;
    }
}


class LinkManager<T extends Link> {
    private Map<String, T> links;

    public LinkManager() {
        links = new HashMap<>();
    }

    public T createLink(String url) {
        T link = (T) new ShortenedLink(url);
        links.put(link.getShortCode(), link);
        return link;
    }

    public T getLink(String shortCode) throws LinkException {
        T link = links.get(shortCode);
        if (link == null) {
            throw new LinkException("Link not found");
        }
        return link;
    }
}
class LinkException extends Exception {
    public LinkException(String message) {
        super(message);
    }
}
public class Main {
    public static void main(String[] args) {
        LinkManager<ShortenedLink> linkManager = new LinkManager<>();

        try {
            ShortenedLink link = linkManager.createLink("https://www.example.com");
            System.out.println("Shortened URL: " + link.getShortCode());
            System.out.println("Original URL: " + link.getUrl());

            ShortenedLink retrievedLink = linkManager.getLink(link.getShortCode());
            System.out.println("Retrieved URL: " + retrievedLink.getUrl());
        } catch (LinkException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}