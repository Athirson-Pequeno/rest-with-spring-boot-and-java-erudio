package br.com.tizo.integrationtests.vo.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Objects;

@XmlRootElement
public class WrapperBookVO implements Serializable {

    private static final long serialVersionUID = 1L;
    @JsonProperty("_embedded")
    private BookEmbeddedVO embeddedVO;

    public WrapperBookVO() {}

    public BookEmbeddedVO getEmbeddedVO() {
        return embeddedVO;
    }

    public void setEmbeddedVO(BookEmbeddedVO embeddedVO) {
        this.embeddedVO = embeddedVO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WrapperBookVO that)) return false;
        return Objects.equals(embeddedVO, that.embeddedVO);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(embeddedVO);
    }
}
