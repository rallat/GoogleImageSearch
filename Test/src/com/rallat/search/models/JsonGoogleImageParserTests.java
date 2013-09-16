package com.rallat.search.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

import com.rallat.search.models.GoogleImage;
import com.rallat.search.models.JsonGoogleImageParser;

public class JsonGoogleImageParserTests extends AndroidTestCase {

    private MockJsonGoogleImageParser mTest;
    // json from https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=fuzzy%20monkey
    private static final String JsonReponse = "{\"responseData\": {\"results\":[{\"GsearchResultClass\":\"GimageSearch\",\"width\":\"1280\",\"height\":\"1024\",\"imageId\":\"ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N\",\"tbWidth\":\"150\",\"tbHeight\":\"120\",\"unescapedUrl\":\"http://www.blirk.net/wallpapers/1280x1024/fuzzy-monkey-1.jpg\",\"url\":\"http://www.blirk.net/wallpapers/1280x1024/fuzzy-monkey-1.jpg\",\"visibleUrl\":\"www.blirk.net\",\"title\":\"\\u003cb\\u003eFuzzy Monkey\\u003c/b\\u003e Normal 1280x1024\",\"titleNoFormatting\":\"Fuzzy Monkey Normal 1280x1024\",\"originalContextUrl\":\"http://www.blirk.net/fuzzy-monkey/1/1280x1024/\",\"content\":\"\\u003cb\\u003eFuzzy Monkey\\u003c/b\\u003e Normal 1280x1024\",\"contentNoFormatting\":\"Fuzzy Monkey Normal 1280x1024\",\"tbUrl\":\"http://t3.gstatic.com/images?q\\u003dtbn:ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N\"},{\"GsearchResultClass\":\"GimageSearch\",\"width\":\"600\",\"height\":\"397\",\"imageId\":\"ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk\",\"tbWidth\":\"135\",\"tbHeight\":\"89\",\"unescapedUrl\":\"http://www.acuteaday.com/blog/wp-content/uploads/2011/05/fuzzy-snub-nosed-monkey.jpg\",\"url\":\"http://www.acuteaday.com/blog/wp-content/uploads/2011/05/fuzzy-snub-nosed-monkey.jpg\",\"visibleUrl\":\"www.acuteaday.com\",\"title\":\"\\u003cb\\u003eMonkeys\\u003c/b\\u003e \u00BB A Cute A Day\",\"titleNoFormatting\":\"Monkeys \u00BB A Cute A Day\",\"originalContextUrl\":\"http://www.acuteaday.com/blog/category/monkeys/\",\"content\":\"\\u003cb\\u003eMonkeys\\u003c/b\\u003e \u00BB A Cute A Day\",\"contentNoFormatting\":\"Monkeys \u00BB A Cute A Day\",\"tbUrl\":\"http://t1.gstatic.com/images?q\\u003dtbn:ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk\"}],\"cursor\":{\"resultCount\":\"6.840.000\",\"pages\":[{\"start\":\"0\",\"label\":1},{\"start\":\"2\",\"label\":2},{\"start\":\"4\",\"label\":3},{\"start\":\"6\",\"label\":4},{\"start\":\"8\",\"label\":5},{\"start\":\"10\",\"label\":6},{\"start\":\"12\",\"label\":7},{\"start\":\"14\",\"label\":8}],\"estimatedResultCount\":\"6840000\",\"currentPageIndex\":0,\"moreResultsUrl\":\"http://www.google.com/images?oe\\u003dutf8\\u0026ie\\u003dutf8\\u0026source\\u003duds\\u0026start\\u003d0\\u0026hl\\u003des\\u0026q\\u003dfuzzy+monkey\",\"searchResultTime\":\"0,08\"}}, \"responseDetails\": null, \"responseStatus\": 200}";
    private static final String JsonOneResultMalformed = "{\"responseData\": {\"results\":[{\"GsearchResultClass\":\"GimageSearch\",\"width\":\"1280\",\"height\":\"1024\",\"imageId\":\"ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N\",\"tbWidth\":\"150\",\"tbHeight\":\"120\",\"unescapedUrl\":\"http://www.blirk.net/wallpapers/1280x1024/fuzzy-monkey-1.jpg\",\"url\":\"http://www.blirk.net/wallpapers/1280x1024/fuzzy-monkey-1.jpg\",\"visibleUrl\":\"www.blirk.net\",\"title\":\"\\u003cb\\u003eFuzzy Monkey\\u003c/b\\u003e Normal 1280x1024\",\"originalContextUrl\":\"http://www.blirk.net/fuzzy-monkey/1/1280x1024/\",\"content\":\"\\u003cb\\u003eFuzzy Monkey\\u003c/b\\u003e Normal 1280x1024\",\"contentNoFormatting\":\"Fuzzy Monkey Normal 1280x1024\",\"tbUrl\":\"http://t3.gstatic.com/images?q\\u003dtbn:ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N\"},{\"GsearchResultClass\":\"GimageSearch\",\"width\":\"600\",\"height\":\"397\",\"imageId\":\"ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk\",\"tbWidth\":\"135\",\"tbHeight\":\"89\",\"unescapedUrl\":\"http://www.acuteaday.com/blog/wp-content/uploads/2011/05/fuzzy-snub-nosed-monkey.jpg\",\"url\":\"http://www.acuteaday.com/blog/wp-content/uploads/2011/05/fuzzy-snub-nosed-monkey.jpg\",\"visibleUrl\":\"www.acuteaday.com\",\"title\":\"\\u003cb\\u003eMonkeys\\u003c/b\\u003e \u00BB A Cute A Day\",\"titleNoFormatting\":\"Monkeys \u00BB A Cute A Day\",\"originalContextUrl\":\"http://www.acuteaday.com/blog/category/monkeys/\",\"content\":\"\\u003cb\\u003eMonkeys\\u003c/b\\u003e \u00BB A Cute A Day\",\"contentNoFormatting\":\"Monkeys \u00BB A Cute A Day\",\"tbUrl\":\"http://t1.gstatic.com/images?q\\u003dtbn:ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk\"}],\"cursor\":{\"resultCount\":\"6.840.000\",\"pages\":[{\"start\":\"0\",\"label\":1},{\"start\":\"2\",\"label\":2},{\"start\":\"4\",\"label\":3},{\"start\":\"6\",\"label\":4},{\"start\":\"8\",\"label\":5},{\"start\":\"10\",\"label\":6},{\"start\":\"12\",\"label\":7},{\"start\":\"14\",\"label\":8}],\"estimatedResultCount\":\"6840000\",\"currentPageIndex\":0,\"moreResultsUrl\":\"http://www.google.com/images?oe\\u003dutf8\\u0026ie\\u003dutf8\\u0026source\\u003duds\\u0026start\\u003d0\\u0026hl\\u003des\\u0026q\\u003dfuzzy+monkey\",\"searchResultTime\":\"0,08\"}}, \"responseDetails\": null, \"responseStatus\": 200}";
    private static final String JsonMalformed = "{\"responseData\": results\":[{\"GsearchResultClass\":\"GimageSearch\",\"width\":\"1280\",\"height\":\"1024\",\"imageId\":\"ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N\",\"tbWidth\":\"150\",\"tbHeight\":\"120\",\"unescapedUrl\":\"http://www.blirk.net/wallpapers/1280x1024/fuzzy-monkey-1.jpg\",\"url\":\"http://www.blirk.net/wallpapers/1280x1024/fuzzy-monkey-1.jpg\",\"visibleUrl\":\"www.blirk.net\",\"title\":\"\\u003cb\\u003eFuzzy Monkey\\u003c/b\\u003e Normal 1280x1024\",\"originalContextUrl\":\"http://www.blirk.net/fuzzy-monkey/1/1280x1024/\",\"content\":\"\\u003cb\\u003eFuzzy Monkey\\u003c/b\\u003e Normal 1280x1024\",\"contentNoFormatting\":\"Fuzzy Monkey Normal 1280x1024\",\"tbUrl\":\"http://t3.gstatic.com/images?q\\u003dtbn:ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N\"},{\"GsearchResultClass\":\"GimageSearch\",\"width\":\"600\",\"height\":\"397\",\"imageId\":\"ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk\",\"tbWidth\":\"135\",\"tbHeight\":\"89\",\"unescapedUrl\":\"http://www.acuteaday.com/blog/wp-content/uploads/2011/05/fuzzy-snub-nosed-monkey.jpg\",\"url\":\"http://www.acuteaday.com/blog/wp-content/uploads/2011/05/fuzzy-snub-nosed-monkey.jpg\",\"visibleUrl\":\"www.acuteaday.com\",\"title\":\"\\u003cb\\u003eMonkeys\\u003c/b\\u003e \u00BB A Cute A Day\",\"titleNoFormatting\":\"Monkeys \u00BB A Cute A Day\",\"originalContextUrl\":\"http://www.acuteaday.com/blog/category/monkeys/\",\"content\":\"\\u003cb\\u003eMonkeys\\u003c/b\\u003e \u00BB A Cute A Day\",\"contentNoFormatting\":\"Monkeys \u00BB A Cute A Day\",\"tbUrl\":\"http://t1.gstatic.com/images?q\\u003dtbn:ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk\"}],\"cursor\":{\"resultCount\":\"6.840.000\",\"pages\":[{\"start\":\"0\",\"label\":1},{\"start\":\"2\",\"label\":2},{\"start\":\"4\",\"label\":3},{\"start\":\"6\",\"label\":4},{\"start\":\"8\",\"label\":5},{\"start\":\"10\",\"label\":6},{\"start\":\"12\",\"label\":7},{\"start\":\"14\",\"label\":8}],\"estimatedResultCount\":\"6840000\",\"currentPageIndex\":0,\"moreResultsUrl\":\"http://www.google.com/images?oe\\u003dutf8\\u0026ie\\u003dutf8\\u0026source\\u003duds\\u0026start\\u003d0\\u0026hl\\u003des\\u0026q\\u003dfuzzy+monkey\",\"searchResultTime\":\"0,08\"}}, \"responseDetails\": null, \"responseStatus\": 200}";

    @Override
    public void setUp() {
        mTest = new MockJsonGoogleImageParser();
    }

    public void testJsonParser() throws IOException, JSONException {
        mTest.setJson(JsonReponse);
        ArrayList<GoogleImage> images = mTest.parseGoogleImages(null);
        assertEquals(2, images.size());
        assertEquals(new GoogleImage("ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N",
                        "Fuzzy Monkey Normal 1280x1024",
                        "http://t3.gstatic.com/images?q=tbn:ANd9GcR8I1844pOFj_ZrHWzyo4SsO7lExEN7jG6Ek08oMJQLN0ECMSsDdUd7R08N"),
                        images.get(0));
        assertEquals(new GoogleImage("ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk",
                        "Monkeys \u00BB A Cute A Day",
                        "http://t1.gstatic.com/images?q=tbn:ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk"),
                        images.get(1));
    }

    public void testJsonParserOneResultMalformed() throws IOException, JSONException {
        mTest.setJson(JsonOneResultMalformed);
        ArrayList<GoogleImage> images = mTest.parseGoogleImages(null);
        assertEquals(1, images.size());
        assertEquals(new GoogleImage("ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk",
                        "Monkeys \u00BB A Cute A Day",
                        "http://t1.gstatic.com/images?q=tbn:ANd9GcRpZyXXWBk0TJuU6PCdvrgrU7QckCJQ5DP96iyLc6uLx1bQn4EvBZDFLCk"),
                        images.get(0));
    }

    public void testJsonMalFormed() throws IOException {
        mTest.setJson(JsonOneResultMalformed);
        try {
            ArrayList<GoogleImage> images = mTest.parseGoogleImages(null);
        } catch (JSONException e) {
            assertTrue(true);
        }

    }

    private class MockJsonGoogleImageParser extends JsonGoogleImageParser {
        private String mJson;

        public void setJson(String json) {
            this.mJson = json;
        }

        @Override
        protected JSONObject getJsonFromInputStream(InputStream json) throws IOException, JSONException {
            return new JSONObject(mJson);
        }
    }
}
