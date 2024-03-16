package servlets;

import utils.resources.ResourceOps;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StaticFileServlet extends HttpServlet {

    private final String root;

    public StaticFileServlet(String fileName) {
        this.root = fileName;
    }

    @Override
    protected void doGet(HttpServletRequest rq, HttpServletResponse rs) throws ServletException, IOException {
        String prefix = ResourceOps.resourceUnsafe(root);
        String fileName = rq.getPathInfo();
        String fullName = prefix + fileName;

        if (!new File(fullName).exists()) {
            rs.setStatus(404);
        } else try (ServletOutputStream os = rs.getOutputStream()) {
            Path path = Paths.get(fullName);
            Files.copy(path, os);
        }
    }
}
