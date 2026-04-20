package student.model.formatters;

import java.util.Collection;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import student.model.DomainNameModel.DNRecord;

/**
 * Wrapper class used so XML output has the correct root and item names.
 */
@JacksonXmlRootElement(localName = "domainList")
public final class DomainXmlWrapper {

    /** The list of domain records wrapped for XML serialization. */
    @JacksonXmlElementWrapper(useWrapping = false)
    private Collection<DNRecord> domain;

    /** Needed by Jackson for XML deserialization. */
    public DomainXmlWrapper() {
    }

    /**
     * Creates a wrapper around a record collection.
     *
     * @param records the domain records to wrap
     */
    public DomainXmlWrapper(Collection<DNRecord> records) {
        this.domain = records;
    }

    /**
     * Returns wrapped records.
     *
     * @return the collection of domain records
     */
    public Collection<DNRecord> getDomain() {
        return domain;
    }

    /**
     * Sets wrapped records.
     *
     * @param domain the collection of domain records to set
     */
    public void setDomain(Collection<DNRecord> domain) {
        this.domain = domain;
    }
}
