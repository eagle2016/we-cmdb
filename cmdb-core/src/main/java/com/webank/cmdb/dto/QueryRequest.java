package com.webank.cmdb.dto;

import com.webank.cmdb.constant.CmdbConstants;
import com.webank.cmdb.constant.FilterRelationship;
import com.webank.cmdb.util.Pageable;
import com.webank.cmdb.util.Sorting;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class QueryRequest {
    protected boolean paging = false;
    protected Pageable pageable = new Pageable();
    protected List<Filter> filters = new LinkedList<>();
    protected String filterRs = FilterRelationship.And.getCode();

    protected Sorting sorting = new Sorting();
    protected List<Sorting> sortings = new LinkedList<>();
    protected List<String> groupBys = new LinkedList<>();
    protected List<String> refResources = new LinkedList<>();
    protected List<String> resultColumns = new ArrayList<>();
    protected Dialect dialect = new Dialect();
    protected Map<String, String> aggregationFuction = new HashMap<>();
    
    public Map<String, String> getAggregationFuction() {
        return aggregationFuction;
    }

    public void setAggregationFuction(Map<String, String> aggregationFuction) {
        this.aggregationFuction = aggregationFuction;
    }

    public static class Dialect {
        private boolean showCiHistory = false;
        private Map<String, Object> associatedData = new HashMap<>();

        public boolean getShowCiHistory() {
            return showCiHistory;
        }

        public void setShowCiHistory(boolean showCiHistory) {
            this.showCiHistory = showCiHistory;
        }

        public Map<String, Object> getAssociatedData() {
            return associatedData;
        }

        public void setAssociatedData(Map<String, Object> associatedData) {
            this.associatedData = associatedData;
        }

    }

    public List<String> getGroupBys() {
        return groupBys;
    }

    public void setGroupBys(List<String> groupBys) {
        this.groupBys = groupBys;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public Sorting getSorting() {
        return sorting;
    }

    public void setSorting(Sorting sorting) {
        this.sorting = sorting;
    }

    public List<Sorting> getSortings() {
        if(this.sortings != null && !this.sortings.isEmpty()) return this.sortings;

        if(this.sorting != null && !StringUtils.isBlank(this.sorting.getField())) return Collections.singletonList(this.sorting);

        return Collections.emptyList();
    }

    public void setSortings(List<Sorting> sortings) {
        this.sortings = sortings;
    }

    public boolean isSortingRequested() {
        return !this.getSortings().isEmpty();
    }

    public boolean isPaging() {
        return paging;
    }

    public void setPaging(boolean paging) {
        this.paging = paging;
    }

    public List<String> getRefResources() {
        return refResources;
    }

    public void setRefResource(List<String> refResources) {
        this.refResources = refResources;
    }

    public List<String> getResultColumns() {
        return resultColumns;
    }

    public void setResultColumns(List<String> resultColumns) {
        this.resultColumns = resultColumns;
    }

    public String getFilterRs() {
        return filterRs;
    }

    public void setFilterRs(String filterRs) {
        this.filterRs = filterRs;
    }

    public void setRefResources(List<String> refResources) {
        this.refResources = refResources;
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    public boolean isPagingRequested(){
        return (paging && pageable!=null);
    }
    
    public QueryRequest addEqualsFilter(String name, Object value) {
        filters.add(new Filter(name, "eq", value));
        return this;
    }

    public QueryRequest setFiltersRelationship(String relationship) {
        filterRs = relationship;
        return this;
    }

    public QueryRequest addEqualsFilters(Map<String, Object> filterObject) {
        filterObject.entrySet().forEach(entry -> filters.add(new Filter(entry.getKey(), "eq", entry.getValue())));
        return this;
    }

    public QueryRequest addInFilters(Map<String, Object> filterObject) {
        filterObject.entrySet().forEach(entry -> filters.add(new Filter(entry.getKey(), "in", entry.getValue())));
        return this;
    }

    public QueryRequest addNotEqualsFilter(String name, Object value) {
        filters.add(new Filter(name, "ne", value));
        return this;
    }

    public QueryRequest addInFilter(String name, List values) {
        filters.add(new Filter(name, "in", values));
        return this;
    }

    public QueryRequest addContainsFilter(String name, String value) {
        filters.add(new Filter(name, "contains", value));
        return this;
    }

    public QueryRequest addNotNullFilter(String name) {
        filters.add(new Filter(name, "notNull", null));
        return this;
    }

    public QueryRequest addNullFilter(String name) {
        filters.add(new Filter(name, "null", null));
        return this;
    }
    
    public QueryRequest addNotEmptyFilter(String name) {
        filters.add(new Filter(name, "notEmpty", null));
        return this;
    }
    
    public QueryRequest addEmptyFilter(String name) {
        filters.add(new Filter(name, "empty", null));
        return this;
    }

    public QueryRequest ascendingSortBy(String field) {
        withSorting(true, field);
        return this;
    }

    public QueryRequest descendingSortBy(String field) {
        withSorting(false, field);
        return this;
    }

    public QueryRequest addReferenceResource(String refResource) {
        refResources.add(refResource);
        return this;
    }

    public QueryRequest withResultColumns(List<String> resultColumns) {
        this.resultColumns = resultColumns;
        return this;
    }

    public QueryRequest withPaging(int startIndex,int pageSize){
        this.paging = true;
        this.pageable.setStartIndex(startIndex);
        this.pageable.setPageSize(pageSize);
        return this;
    }

    public QueryRequest withSorting(boolean asc, String field){
        Sorting sorting = new Sorting(asc,field);
        this.sortings.add(sorting);
        return this;
    }

    public QueryRequest withDefaultSorting(){
        this.descendingSortBy(CmdbConstants.DEFAULT_FIELD_CREATED_DATE)
                .descendingSortBy(CmdbConstants.DEFAULT_FIELD_GUID);
        return this;
    }

    public static QueryRequest defaultQueryObject() {
        return new QueryRequest();
    }

    public static QueryRequest defaultQueryObject(String name, Object value) {
        return defaultQueryObject().addEqualsFilter(name, value);
    }

    public boolean isColumnSelected(String column){
        if(StringUtils.isEmpty(column)){
            return true;
        }

        return resultColumns.contains(column);
    }
}
