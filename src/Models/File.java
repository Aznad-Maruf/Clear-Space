package Models;

import com.maruf.Utility;

import java.nio.file.Path;
import java.util.Date;

public class File implements Comparable<File> {
    private final Path path;
    private final Date lastAccessedDate;
    private final double size;
    private final Date createdDate;
    private final Date lastModifiedDate;

    public File(Path path, double size, Date createdDate, Date lastModifiedDate, Date lastAccessedDate){
        this.path = path;
        this.size = size;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.lastAccessedDate = lastAccessedDate;
    }

    File(Path path, double size, Date lastAccessedDate){
        this(path, size, null, null, lastAccessedDate);
    }

    public Path getPath() {
        return path;
    }

    public Date getLastAccessedDate() {
        return lastAccessedDate;
    }

    public int getLastAccessedInDays() {
        var diff = new Date().getTime() - getLastAccessedDate().getTime();
        return (int) Utility.getDaysFromMilliSec(diff);
    }

    public double getSize() {
        return size;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @Override
    public int compareTo(File o) {
        return (int) (getSize() - o.getSize());
    }
}
