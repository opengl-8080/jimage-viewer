package jimgv.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.File;

@ToString
@EqualsAndHashCode
public class Page {

    private final File file;

    public Page(File file) {
        this.file = file;
    }

    public String toUriString() {
        return this.file.toURI().toString();
    }
}
