package de.lmu.ifi.mobile.msp;

class MetaLink {

    private boolean hasBeenVisited;
    private String link;

    MetaLink(boolean hasBeenVisited, String link){
        this.link = link;
        this.hasBeenVisited = hasBeenVisited;
    }

    void setLinkVisited(){
        this.hasBeenVisited = true;
    }

    boolean wasLinkVisited(){
        return this.hasBeenVisited;
    }

    String getLink(){
        return this.link;
    }


}