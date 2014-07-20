package jp.mobs.gogo.android.dictionary.model;

public class TagModel {
    private final int id;
    private final String url;
    private final String title;
    private final TagDefModel model;

    public TagModel(final int id, final String url, final String title,
            final TagDefModel model) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public TagDefModel getModel() {
        return model;
    }
}
