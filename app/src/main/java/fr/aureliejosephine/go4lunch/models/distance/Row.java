package fr.aureliejosephine.go4lunch.models.distance;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Row {
    @SerializedName("elements")
    @Expose
    private List<Element> elements = null;

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

}
