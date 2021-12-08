package com.vgearen.webdavcaiyundrive.model;

public class Page {
   private Integer endNumber = 100;
   private Integer filterType = 0;
   private Integer sortDirection = 1;
   private Integer startNumber = 1;

    public Integer getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(Integer endNumber) {
        this.endNumber = endNumber;
    }

    public Integer getFilterType() {
        return filterType;
    }

    public void setFilterType(Integer filterType) {
        this.filterType = filterType;
    }

    public Integer getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Integer sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Integer getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Integer startNumber) {
        this.startNumber = startNumber;
    }
}
