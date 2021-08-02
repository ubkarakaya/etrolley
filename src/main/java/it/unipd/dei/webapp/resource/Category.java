package it.unipd.dei.webapp.resource;
import com.fasterxml.jackson.core.*;
import java.io.*;

/**
 * Represents a Category
 *
 * @author eTrolley Group
 * @version 1.00
 * @since 1.00
 */
public class Category extends Resource{
    private final int id;
    private final String name;

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public final void toJSON(final OutputStream out) throws IOException {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);

        jg.writeStartObject();
        jg.writeFieldName("category");

        jg.writeStartObject();

        jg.writeNumberField("id", id);
        jg.writeStringField("name", name);

        jg.writeEndObject();
        jg.writeEndObject();

        jg.flush();
    }

    // fromJSON method is not needed
}
