package jimgv.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.File;

@ToString
@EqualsAndHashCode
public class Page {

    private final File file;
    private final boolean ignore;

    public Page(File file) {
        this(file, false);
    }

    private Page(File file, boolean ignore) {
        this.file = file;
        this.ignore = ignore;
    }

    public String toUriString() {
        return this.file.toURI().toString();
    }

    public boolean isIgnorePage() {
        return this.ignore;
    }

    static Page ignore() {
        return new Page(null, true);
    }
}
