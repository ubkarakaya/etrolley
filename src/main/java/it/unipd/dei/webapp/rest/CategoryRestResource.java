package it.unipd.dei.webapp.rest;

import it.unipd.dei.webapp.database.ListCategoryDatabase;
import it.unipd.dei.webapp.database.ListProductDatabase;
import it.unipd.dei.webapp.resource.Category;
import it.unipd.dei.webapp.resource.Message;
import it.unipd.dei.webapp.resource.Product;
import it.unipd.dei.webapp.resource.ResourceList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

/**
 * Manages the REST API for the {@link Category} resource.
 *
 * @author eTrolley group
 * @version 1.00
 * @since 1.00
 *
 */
public final class CategoryRestResource extends RestResource{

    /**
     * Creates a new REST resource for managing {@code Product} resources.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public CategoryRestResource(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
        super(req, res, con);
    }

    /**
     * Returns a list of categories in JSON format to the response output stream
     * @throws IOException
     */
    public void listCategories() throws IOException {
        List<Category> cl  = null;
        Message m = null;

        try{
            // creates a new object for accessing the database and lists all the employees
            cl = new ListCategoryDatabase(con).listCategory();

            if(cl != null) {
                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<Category>(cl).toJSON(res.getOutputStream());
            } else {
                // it should not happen
                m = new Message("Cannot list categories: unexpected error.", "E5A1", null);
                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (Throwable t) {
            m = new Message("Cannot list categories: unexpected error.", "E5A1", t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        }
    }

}
