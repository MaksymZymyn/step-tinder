package utils;

import java.net.URISyntaxException;

public class ResourceOps {

    public static String resourceUnsafe(String name) {
        try {
            String path = ResourceOps.class
                    .getClassLoader()
                    .getResource(name)
                    .toURI()
                    .getPath();

            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            return path;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
