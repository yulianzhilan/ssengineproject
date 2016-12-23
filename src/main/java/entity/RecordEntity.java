package entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by scott on 2016/12/22.
 */
@Entity(name = "record")
@Table(name="RECORD",schema = "crawler")
public class RecordEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @Column(name = "url")
    private String url;

    @Column(name = "crawled")
    private Integer crawled;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCrawled() {
        return crawled;
    }

    public void setCrawled(Integer crawled) {
        this.crawled = crawled;
    }

    public Serializable getId(){
        return recordId;
    }

    @Override
    public String toString() {
        return "RecordEntity{" +
                "recordId=" + recordId +
                ", url='" + url + '\'' +
                ", crawled=" + crawled +
                '}';
    }
}
