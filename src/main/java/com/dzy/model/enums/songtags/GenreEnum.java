package com.dzy.model.enums.songtags;

/**
 * @Author <a href="https://github.com/dizzyfall">DZY</a>
 * @Date 2024/5/25  21:37
 */
public enum GenreEnum {

    POP(1, "流行"),
    CPOP(2, "国风");

    private final Integer genreId;
    private final String genreTagName;

    GenreEnum(Integer genreId, String genreTagName) {
        this.genreId = genreId;
        this.genreTagName = genreTagName;
    }

    public static String getGenreTagNameById(Integer genreId) {
        if (genreId == null) {
            return null;
        }
        for (GenreEnum genre : GenreEnum.values()) {
            if (genreId.equals(genre.getGenreId())) {
                return genre.getGenreTagName();
            }
        }
        return null;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public String getGenreTagName() {
        return genreTagName;
    }

}
