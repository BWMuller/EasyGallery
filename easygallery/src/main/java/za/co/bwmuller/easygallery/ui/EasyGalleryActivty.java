package za.co.bwmuller.easygallery.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import za.co.bwmuller.easygallery.R;
import za.co.bwmuller.easygallery.ui.album.AlbumFragment;
import za.co.bwmuller.easygallery.ui.media.MediaFragment;
import za.co.bwmuller.easygallerycore.Config;
import za.co.bwmuller.easygallerycore.data.custom.AlbumLoaderCallback;
import za.co.bwmuller.easygallerycore.data.custom.MediaLoaderCallback;
import za.co.bwmuller.easygallerycore.model.Album;
import za.co.bwmuller.easygallerycore.model.Media;

public class EasyGalleryActivty extends AppCompatActivity implements AlbumFragment.OnAlbumFragmentListener, MediaFragment.OnMediaFragmentListener {

    private static final int MEDIA_COUNT = 2000;

    private static final int ALBUM_COUNT = 10;

    HashMap<String, ArrayList<Media>> albumMediaMap;

    @Override
    public Config getConfig() {
        return new Config() {{
            mediaGridColumnCount = 3;
            albumGridColumnCount = 2;
            loaderScope = Scope.IMAGES;
        }}.addExcludedDirectory("/Camera/")
                .setCustomAlbum(new AlbumLoaderCallback() {
                    @Override
                    public ArrayList<Album> prefixAlbums() {
                        ArrayList<Album> albums = new ArrayList<Album>();
                        Media media = getAlbumMedia().get("-2").get(0);
                        albums.add(Album.createCustom("-2", null, media.getContentUri(), media.getDateTaken(), getAlbumMedia().get("-2").size()));
                        return albums;
                    }

                    @Override
                    public ArrayList<Album> postfixAlbums() {
                        ArrayList<Album> albums = new ArrayList<Album>();
                        for (String key : getAlbumMedia().keySet()) {
                            Media media = getAlbumMedia().get(key).get(0);
                            albums.add(Album.createCustom(key, "Other " + key, media.getContentUri(), media.getDateTaken(), getAlbumMedia().get(key).size()));
                        }
                        return albums;
                    }
                })
                .setCustomMedia(new MediaLoaderCallback() {
                    @Override
                    public ArrayList<Media> allMedia() {
                        ArrayList<Media> media = new ArrayList<Media>();
                        for (String key : getAlbumMedia().keySet()) {
                            media.addAll(getAlbumMedia().get(key));
                        }
                        return media;
                    }

                    @Override
                    public ArrayList<Media> customAlbumMedia(Album album) {
                        ArrayList<Media> media = new ArrayList<Media>();
                        media.addAll(getAlbumMedia().get(album.getBucketId()));
                        return media;
                    }
                });
    }

    @Override
    public void onAlbumSelected(Album item) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, MediaFragment.newInstance(item), item.getBucketId())
                .addToBackStack(item.getBucketId())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_easy_gallery_activty);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, AlbumFragment.newInstance(), AlbumFragment.class.getSimpleName())
                .addToBackStack(AlbumFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void onMediaSelected(Media item) {
    }

    private static String getImage(int seed) {
        String[] images = {
                "http://loremflickr.com/320/240",
                "http://loremflickr.com/320/240/dog",
                "http://loremflickr.com/g/320/240/paris",
                "http://loremflickr.com/320/240/brazil,rio",
                "http://loremflickr.com/320/240/paris,girl/all",
                "http://lorempixel.com/400/200",
                "http://lorempixel.com/400/200/sports",
                "http://lorempixel.com/g/400/200",
                "http://lorempixel.com/400/200/sports/1",
                "http://lorempixel.com/400/200/sports/Dummy-Text",
                "http://lorempixel.com/400/200/sports/1/Dummy-Text",
                "https://www.google.co.za/images/branding/googlelogo/2x/googlelogo_color_120x44dp.png",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQig4Eb6L8sbMKJ8pmYUDPGruaQnEEf27OzGT1LjLHuHxg_NuvN",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQeo8cw131Hh8jFarKZJLivgQRn2ioxreUJzVYHLfujB2gORW7b",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzPp1tQ25SpK45TRjEqkj8h0eKHjg7jxrq-TQqpduzOI-sWkrl",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNC3WXIfGNmbwXV2DNrfZxyh28HPvEz8EL4A-Ckf3Uwp-vkw6z",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqA2-mX52L-6xoTNMw8FJ4uLa0GkKuSdC0AwK_4o2dsvfpqMdrbA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsjGdb3AwQz1CKo4gcizMmiWJ1PwLy76Bsow9nlfOIYxQLZdc_jA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSckC8Mya48G2NkoagsP29os2ooWJ-ZvexWxOeD3sWQrWw4bDzgrQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTbLrhPmO3WxWi2_CRPLkn7IoyDApuKxD4j2odgkUtBEigZuFzs",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9MYhAvzPBZmyH9RcmrY_kmGtRKCWNEa77v10q7P1SStHvP82DgQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS0zY_MFePoIrctAcyARbh6m7S7h4huvcBm1qjGj271U388e1b6fQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj1WTUcNJ01SOk1I1q7S-ql8PLjFYP2dAR-htc3wp6l_gB-1mCoA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQIYvJFLcbMMX2FvJ2skSJ2iLygjvUDlyC38dFmyizMYWvsdnvuSQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYWjhMlJssKU4iZw3z59i9nOW7RN50IDoXsZusw9RMBXCZw7tw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTSu6g99BsXRFHpg5Ge1Oh2RUZLDkfzWwnivTfmMwHAJI3Hd_kd",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQLPVIlhY4qe_aAGDo1iMLBKhck39Xu_RBqTlBW8r2KWa14mKhu",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLZDAac6flbjCchY4-e8pcqE_By4OsPq31AtUnJ5NuqvCJ7Z91bw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT08AEq7vqGvGphjXTexxowwba7HYzQ9gv57APvR6RJO-FNNmlgig",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4QvOwZEp0O5wRrIDGFRqFG2nt55FUIUNqldcVgq3jqmQLPJsHvQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT5yPdhRVhTKVvWIzRcszD0SocIc6U2DRzttNIdl8j3UuPQJRSBLg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSaZZUYkoIfhmLQvyGDzGRKnx1A4ROnHJw4X2Ui112T7NBxXe6o",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1RzzK1sfwoJfVZVRSD3bLlzCN06gCfyYskW1pjV1FRMyFBJcjMA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ7tY4WwS7kzZDd2glb9dYujq1BSXmpmjyPQUi9HZA4o6XPvMPX",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSyyC2RaRe4ckDHR0dGGOpso2AnCzTBynqCUVEEx-lBqgDUtxwM",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRI9jE3FLPrPBiF5XMKnKtO2-ldGI8QpabtIFCC83t0Nxuy8tgY",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQl5RfMOvlnsLS-MghycxoIVxxU57U0GVWf2AkFhbNkK3qtlE11sQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSXKvny90Ya4uiJPjIJdi0c-BU-gEvhjzNJHWy_hGTHt2X6bW53Lg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSLTc4ztFJBHih_1ub9FTWsQ4H77LsH58tp_eNTg4W-8WbLVExC",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRiA5gbBG5RbZDozvylUKSZhXUjwsiLEYyGK8G3O_uj_hCZVv2Zig",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR9sEWeSB5JkXRBKOVmKRuHRBV6Zu9LFvdOHBhn5jcGMZBRf7m-5A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRw93UeSupqBrtbKXOIz7xyTAo9RupPSBvq1BfEsWCnoKkrVdJq",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1BJm-K15NVkuAplKwget2AVs1OtTA4tcTlKgUwwjLWsRpeOdh",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjQ9TKEzfJDklUujr-CVimMA7frkjh1bwsiPeRAE3aQcl2mzF2",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5opSxr6dTySarbPW4ahIwdUuaMf-CGR7JmAKal7FCh6t3oD-U",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEu8GizPdc-b-1svWRvZaeBILuAPfvfQBW2sKIcyFvlvqqO2pNmA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0vNj_mem9_NGl203-jvp9EVwsXldzbwWsPPlvYoURc8vt6jNQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSqlutv5oySpU_stAfVdxKDnU7ZBkmO6Q_oRxy5lNSPQiDxdYhy",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsPcxPzD_H3yFpuejRq8qjxpsKUqLnQO4FaCsM21N1PKcyoEpirQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSD0U4FlSacg66T5ymP5FMVQV5dUwbGZeZC1rJCSGqDYRe2fX4hBQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRya5QT2zfnuHv9Xdgg-EUCGZD9YCeWphH3tg30zereGH8vXR-I",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScVBKCZJ4HU9N_g_0C4RZJzbUtta8vpuhTNnaJhAvVxtUKwOn5Sw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMp_O3kMQV2PYtbuiKTZORuC4yDMoVz6lHdN41jbGi7P1xgqOjQQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkcosYZ3ZQh169_lZxwxxxk5sQHraDrix5qxb8VWnLIO6o9V1x",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbegOOLO8OIryreCx9TowL8QrYU8jR0eIdAggctdoZnrHNnwGy-Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMCvzGP6of99Xqf_ZpzlLToBhZvmeTLJJm4GCi7jio2RktncanVg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQkoLrDSZDn9krPQnuVuUG6iW6pqKbHt4iiucSmb-EUT2QI3r2-lw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRt-ZBvfs8aEcEhPpSH1NbVbA3JqMghasWYcuI9LNIhUHmkORuSXA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiAaNGuVY3o92n49g1rTduDpJp3jMZlWxravxm3H9f336TADax",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRfyuCp2I55rOHGHAVlh3BQ-FNkl1FXTiQzB-evbIG4msdMy7xK6g",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRp0vb1kZigmS8wRZ6mIiIuzl0lWyEwbB9Tf4fEWdDcfYcWK3dFtg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSsKXENfXE02dhlt_HjT_bApzBgM6nx37Ef0m9dlFhUbPvyC7Bp",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTkn0VjcWDJoEUqG3hGZJzxct3YhgMfdBXgiBKOUhUWKPpch05",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcToNkIf4e9b7WW1jJWZZbAGo_nKYKQBAAdy1ZSeTZy243aFOQKL",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSwHYitYpld7Ojmh3FZnylKrVKFQEqCpAKkOCNShgSsDDHLx8RbYg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT3Sl3Kgr4EIgS2Ek1OkuFkuKrg1jZ_ke6FwgzPDYhgqFnEmI4O",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-Cv2OZILZUvbDNOa2VdmUoagLsxASAXCgv75rgvpOsalJz2sa",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQigWqtMZTV9Rbgn6_NEkF0ZxYf5FpRdLJI8tG8FBCrbt90mAdk",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTmonO56BO_xV5sSxQ1g7Ik97Q7_7eq_9LfCnv3WK1O-d7nfL3",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFt6Nr1v5nOOjNnw7jEuXghDOzwofFqfQ7I1xcVMo2zuGcs8LuQA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzzQeCiqoxmJcNZSOKZKVVx_O2LbxTN-ZubLBOg3Vz1NKbrDr9GA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTED4hHzRWfetCJeQ6s2fTIKcN7E2BRvaGJGz8pONmwi3jWJsjx",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZC-ZDxpkuf750VCBJmMsXzvXcb0tcu4094xLZdEGiFLL_rzJefA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSBgj3s9Vl2nXKFwJNFIQ7e3q_6nEx-nhXVSrCqWkymr5llBp-b5w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRfTSHT6pnPsxje0NNo8qIvysT8aIynzsvfBtF4yAbdTOIxUH2C",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVz0snnHfSosoW5Va783o3IHYt4JWUArfYc12jg651-fJHZOl_",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMZ5F-2Dl12bz-z1PjghpEDhwsajxkBMiSz7E7IdiExQaJPsAqPw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR1XV8xtEtYC1_6-FzJ9oLlUOH04B4L7aFU4bTXbdI8WZvyj_9VCg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ291xELsemxOV8gaTq54JI3rme_ely1D3v0-g5F4h05cgda69m",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8djLAoq2qa0fhALXsAFYVZuM9cr7yaBXlRLWkQtjr9EeF3hrd",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQOFtlXMeXnDAg-Ua5oAbcyYROySzCsw4vBNU6FFqxnNtBUswNgQQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkxJBBwHYQLv3nugTlnEEBj2nQHWSKQ5tXNIGotbcOER2rCFjxDQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTX5JLldxRSlrOq9_ut_Ain2y1aq9dWGq1dtFwb450qBV9zuF-Oqw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSb1-nGirY2cbNXndpGThGEJrLSTV7IWmfWqmuBkW03v3huCmQTrw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-vcFZn31GTFlVelEAiE6XA5G7LtTlQQ9cr0iRx9l0_4MCVOdC4g",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTNaMThRnKxNlOKC5cneOGHkVZX1rR-3HojSH_-iV2bOIY8pXlg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1_tNkjGibhWS-Hy1I1N0WNxvR7nvWV4TgHB_vxolPtWyi6vOs",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJo9tzam48izncUX9zmbC7YaQIlPc8TrG4twu7cBkJLhYwhSfL5w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ29YGjaF-BPZhe4Jhk8Y1Mln856Zs0sKxVjcnyn-Yn4jBAgha0_Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRT7-znus6LkiQqY1XjI_qGIXoZcSkVAX6cOhC6u_s4-RTyDZhX",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeCC41tc0Gl7BFtg29H9LrJXw25MUzsqnelot92J-t36PqRlAd",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5S43Oa76M8o-oRjYsENKW7BZcklAgXujuf6DJnsvnxRP-MIezaQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7YjrXBanG2BfRF5WQGujfUq4jl-TRXSJ2lwXz0AQzGqc_O153Zg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSfm-MHihTbIIXFj4_rRowhe8pl-2uZQ-v94_GpWXPriu3sjreq",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_P2NfGy1P1ki7nk9IZHApmx7vuD27P2gQIqo3FhvfWlC0okykjw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSDzBOdpvBP10sRZI13UufpeznIBYBEX7kfoK3b2-RlvsQXFIEh",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSSQv4-yYGbqldlUT2VvJQHL1eSZwu-Kz0ArBiIyH1LOkAmV4u3",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkMm2Sqe_Q3dkDAHz2f9rVKtgTgv01VSFZ7hRlkHA5qP_yQtVdnw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeDVhv0XbDoVt44zD670lB3PXZxLfH32OQNjjCeUtSfLomT3MyIg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRHkWHyq7s_kQv1CbYlQ40oUsEM7jHJaWmirzUZLrlnIUGsHtoO",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSrAx8FNgo6e3ZRVgcDKDEeK7Ig8t-i89cJH6r4b1ajQ1pB-YCQhw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTpElloKsFDSn9FCxbR3j7v0dL5DwBI7DQwhwhaGBiidsj6Y5csTg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQy6JWkcj5Vze06JAGK00TSnjTsLgW1bDnmuZkpGxMcf3_v0YpA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRr3Hs-SnNZBH8XRrIbtwlpFw2sm7BHhbBjN2L1PvGpVKWOKa6eYg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSj5LiKP-ymc4SUKryGMzU1OLrXK30i3zXUqJMsYayar_qrdEqo",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSycpWDWpFIIq_LcFOZ58Q67Rb5AUe8GDR5zY8PGtiHQOsVceTI",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQUIFhWxLYaWO-5_tSVnW0_FOW0qrJNVcoSv9oJ97h58X7pe0HD",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqpeWutRscMD7eg3k4txeAzBhlk930QI4XnUI7CL5FJ0AkQr1v",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTLDQnMXhnhCz2XcaCR6h3j6txBnCw-Izm5rPoSwo8yrOsqLBxLTA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuL6o82CcG9-J1ru0CTQpvaSq2N0jBLkafG5ct55BZl0UoUPwV",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjPF0fj7MkF4TmND_Wea2tcXrxatBE-a5JFvkW2ESe-8zAvJ2xtw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8RmLIRL3puP-LyxXTIlJrgTgJAXnuxzm9vtcD5aet5UIcrF7-Tw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLRNJenFhIcL35W3S9-xT0skCZX0mf2uSV2ZSLeHlHJpDBBuMt6Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-FEpkNeoCtUhHnwGqwzloyUl3Uu_KiZZnOmnvH4SLAqN_-aP_",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsZc3fmiCbCumvScL8nnmbjwG9ABro_DdHHNS4bGzqWduY-A6uGg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVS5rBUkTIns-vlhXS68CNwFp7DDS1lk4hetsaNnuzSu8lxNB0",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSuzlqhPBDgVcucM5z-82IAmF-xPNyvk66u_UBFqE-MwrnlFdK07Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTrxVVOFzXzpuChSq7KODQOQIdlj8-WzWVUuxZxfy5iKeSB7ojAVw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgFEJBTGCFhjaU9JohI7IwOdSuC5yr1-SUEVTtATt61A9meL0G",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTx7eIPKG5l78NG6X3AnlqmgmA4-Skz43s_xmIj4JyoqC6zPSRk",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMgiMSvLvI84-4MXxbnQYBhvCK39LL0WOMnUkVDn_uuphJ_BkOCw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJrQFlp2SU6ZpQYxrz14Cmm1ob91xXH-9XxzK4PIbxWAFT4QTHYA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmMLikMLjFB3cHR3UG1q6rKYzM2om6qi-4wbmyGAHsJllaI0XS",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcCHDex0NzwT1LaD6WkFSjo_Q2W34V6cpejA-2qO5cQkdlRvR5",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfoh4IdQjYMaFyspnx6-r6xDZRDvO1tNQQcSS1loKItUVoz2DyKg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRKvwigz9b_JZ51-OwIX-izYlamkBMUqIepfAfUe9y94DNmkOUm",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQqJurXL18t1u9-fJD0sUeybveYd-_9tyRRpO9GcLaEUwSzjiq",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSckPP4xbDxGtJsM-Fije4kkSEh4tl1g_nyZe-yWdP8n61G1e5N",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSpWSrdbcpArPusg8Aa42jjjsy9xkPHM8pS5Kx3m0tpFqXM66s_",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSDG7cD9Q8eA-dt1IAjFhVCPW4MvhjzEJo7lTvK5aMFM1X-Gnpo",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQo21EjvpN0X-L2FwhtEIM2yO4hY0PxWfSMV7zhr1X0wn6kmwHf",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-YLfqA2rWozKvTR6ltFLZUjxKC40rkGpbUxqrv3O8QYf-Lpei",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAtD6Ez-t79aiO3cub1PVPxqzNxEUJjR3nFcGcxiOtA4NUKxmUmg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTcjAcGf146Bo_BQ0r-h7zHUXErkIRTHYCD0z4sMvGQkHVQYZRn",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRez-TZZNUpFhzAB4hH6I6RDxPRZdQOQSQO8JbZI9NBlJbGpjX5",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR4wqEi3DJt6YPizYjYVFh_1OMYKRJUJ9haGu5ljJQMBYrZo_DXjA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTi_QiMVZ3HY4jYxdfBIbt0GCL_5Gvqm38FWC0leAp-c3YgkZX5",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSk3C6LA0BcxLXxZfW36r-8IPHZJUmExtiJAXDK1X40IiYT-dKj",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTeaSyabJwrxfjMJ886lAU-r9WPWuUVuWVI_0t7pY28saD0QIj9",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3TcxKHicPbajYh9hy98ipoxzfdOhRmqnqZ_OHpYFIGwS9BrxRZQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSshn0nli1W6jO3AqFgtsGzjAt4I9ieDMad51JEhQi3KPjUJeKx",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNnHcaYiQZXpOyubM2T10f1ToJAjMgN3ed1e-jB0xAy3PS8_f6",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ_UEIxJfKAoayqh-PN3IsVy45kc4f4Bh-iJ-28LXcOgjDrZwUS",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT35fPud6jwskkteSzmypFF2zF_oMtzmeXZ9fLYGfTu8exGc5DRMQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3xh6ondNmbOzHYgsxD7bDMRsPJSHMpqn0E5sOprPgkqqyou-j",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTT_88FvmWTomXyZ5xrAUl9F9rXCA4TmK0QxfAl6vpV2Me-EcZMug",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQO7OisTD-jeQQwak9FGMbrPzkFBRiOI_VEe0D7-NdbJ0yGYMHw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSQeM5tTqlMgTIJehGTBWHVahcArNUBj-HIaROzBZnw9atRuWm",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQoNN9Urm4ljcd5JizmVtKACjxNJCLojBZLNkhkgYMke3PRZ1rgIQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQt4cMmqDh-jqavx9bugO7og2SZsZ0Lx_6uSLGrzirJkQ-02nCVBQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDGdhG7XjbQYAS4gSHegQxwgrCy-ixWwVHRfFVDgFahn-RIOwFaQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqDuzHYR9XmieXwTqdZe3F4KWAB2-ttmR2SIbeKOmqFqeAbNRn",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQy6U_BLwqufCIYZv1m5-U7pn7_z5GduYNuZUZRP4FmCXIKb_XM",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS0XSH6s_jmZ43LEnu5yDO3zYizAkQOdea2SEb1d64XGKaZ-NsT3A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7HzN2_hnBQiwSYucV4C7sLmXDDzrOtg3GOGT2R2eTMO_WvFos",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJP2kG3Bdd5_s0HofwYowXNmkUACCngleQ0qCrlaXt8KsS6Zf-0A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_zcJyrt5d1i6esm_LczDY2ixf64jtBKm0IsyQxr3yTyO7LcQq",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRK07TmGT21liAzLgD_K9bAw14hs3uD4ULTD2iJJQn5yAleCtPPvA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQA7_W2Mb86ly4HotLoNAzBeX8KiI7Baq0IfQ1YMHHasv89_ZFX",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2SobtPAKd1j5dFlg5Q1VDBUHm49IvFqlsFzakrljSB50v6rYKVQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSWwxb9NpiUb5ghRq7aYugOAffkkLq3efra2mTCeqNHX-GUnqh13A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTxLzhmXR3xIM7yI2Dp7iOPDwpSZCilPeilmviOkUwFbhvvf4pB0w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2_TQb-AWqeLzXSGeWZaXLKlJ3h_mJY4rrUUSOgXjReRgrjxwi",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQmM-L5bIvRyuPaO07a34d9EiEaBwZRcrtjbIvtP1r4FS2NWQ2p",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTHiakt9Z7S8G53aBAZcwsT59Bw_a5KJTiamKmZXu82bQFuw3hN",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0iD0zHzfV8VRKIBrfErAMOEHytzMEB67mtIjcgz3yp4xA9FsbTA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvGXuHoWTkRi28eWiosZgD80nFams53fKc1mJJlNfwVQ865SrX",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQv0qHrbGKRT-CiUmImL-XkNcL7pXcNAFtX7KoUdeFzuOBknV1p8Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRISmPfDBYlvCk-NFgDTchl-eO3aKlm6vjSkWpp-vqjqrUjlsF6",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQEF416SIaJNNV5ZpDbfisSc7Ju9bjFY4HbSE5E9byGjGc8iyRA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ3lUeU9XSouxg7HDSz9EGAEDV2RIE6_HsW5SwUJm6dJDh4r4Xt",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLCJBHhbKKIrtqN6_pEwJhiO_SNl6aVAhT6rM7jvscMBZSF9t5Sg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQI3OPABhKfbaosWvfI8uc329yOFCHVxFUxGcUkFFNH6kMoBbkT8w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQE9Cn93stQ1ZOXGOvZVbuaNGuOywb5vGfgCSL-9E4gs_7w0q6iOQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_5-fPGRqUYjHnrAKQZCP534V_h-k2sSutADL4iz2zMzBCVTYmeA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7TiDFc9mvCj36_RNvcae7ETJ6jThnxu5aKsh0Sgyk5wuL_NzoWw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnPOwczs0X4mGFGyWvgWP_T3OG8rdoEvOlwUANngyGDsk_2nFE",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRrcHXinL3M-MTpsfQNVAnljYNbW1dXBfYjfHp45D1nRrARKuLxDw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVVP1Gv-_fBeuJuMuHnxD3zj9ZrtRavg3zUxkgFAClgBWOB6NvXg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvYjIufBS4DgkacllSd55pE1ac-_CkWPBHU-Gge8bOnz90kQMl",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsOYSGumSPj6El-9mqMCmb3gBHU3OQRwsv-LxIjsGiH7CNhZfn8Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSYA72iPNo9nhezOf6GCNnFOjQSC5XzARA8FaFOi_S4Wv4MWu-9QQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEPtFIoiyzp4LnsIgQ3zH128ouE6SsMqOjg4vNpikkRiL-7qGp",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5MU1_wbOMvgxbmN1D9wLbez30hMu8Ve-8JMWq6Noi9W8sRlBDZQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQJqI8kb9e1KeV1qmj89f4p_VFTl5IRl2CD1Y3vxyMvF4Up4-LKkg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScKHEN1jjvdrHoVm4VxeLg-Nxzyd3tu9R93tBFzIMgRCqHbNuf9Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqanuDw6LJtumZT3hzvSHQrifumgPz6hc_QFK5UdCDeiomlGuyiw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHs4VPM1M8fYnFCbkqgpNXHYLUFc8X3Knerh4LqnvAtzB8QZcYbw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTilMUktaEF1YJyjTIl_9gxbLbS2JNt_TkKNFH_nGKCFDQ6ScEGUA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRRbwOG5gkFDQAhz4zoDiTHbSApcooUxpFRYNNpXvrrMYBJdU2_ag",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSAOteyKL_jWTkxkdrK6J6YfLkZCczHYVikiRRfLrsQ-GfWl200Xw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlLphIy1G85ccA-xJ0OSS04pdceanZMUatbZ8DQtYPkaPXTuvH",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQnxTd5N7u_ilhQVu8UCVC-GTtm_1Y3fRdBb-ArZwnR-pD3RG8ZHw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5FNWhQC62HIN1BsRN9LzoKlBnLU21JoMN0YAFk-qNDbRgnOCtUg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScqLLTFhYtM1iZS-LvchAr5lnFQvlgi1KxPShimalR2p1acDR2",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSe2gsCXhekFRztxPYSFEOX1UvDIfRqfD9D0mwoQ38OCTL3ZmdW",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvIyq4VlYnSa_Nz8-1mEj6DTam85JG_xH4d7prWeqxRRkzq7Jrgw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRpoI2t9jb1uC32-oNfEeI0G_QK-yGEUY6Hk1ZzkvkKMNqNwp4uKQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxe8l_XAhkZjJ_jhN5jmOwIH4kDTUVj4WWQLsJQwW6zbjG3PTX_A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSetA-3Yu-NWIZtxzZ_uRMVkilH270O9kVgOwTxcnCKqURiFmza",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ74YuQli0AAVTzr7oEJZzGvvPZaTtJ-Fgponu3CYzAcLbWNidKYw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlPAlpcTDTRooo2pOGxuaXlp2_03v7W_jUPiZfXBld6zj1ay2zfw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDVy2AUGCDcc-pfLu69w8l8y4ewK0Sg_i_d_nn8DFQt4EmNWrU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTGcIcKDwzRMq4HFaKUbyorJlJDT0DXyn-t6wgZUr-29HzF22hw9w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR6d8RUGs3Qc3YIsXRXmw_W-c56Ms0bzvaqmrHIvY4DIrs06Pfn",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxmQ-S8ZoGuPkFk0I_icMrEFH7soyFcP-3uLVaiQvquXfxiOIivA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQZoWQr5vKhzrQAAU1TDzL8rARzv9Hw36CIBlDByMq8FZLaAhnr",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ2vmhcbEZO0stOoguIiD_8fd6QxbtgU0y-KsgH4K2fzQCU3MGOAw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQu6-n0LWEOtEDruItDUp_1NW6a1OEwngssiuJlZdzQSgqrHW04Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTQzLxPAUkb8rVimsTV7r8dgU-tCdPzJf9kFv2zo2-eWvNAXiTt",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4PkLNnGFeFM7YVh8IzealtkyB4MEjmHFkkqs2qBuKJDMtMZyXJw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQaUrD9nz2Kp1mBJwsjKUwoHr99uDQbqtZRwJ1dU85T28ETwZ-W",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQv_i6hc9kDV0i8eMAAPLPtFHlDb9W3UN9ziKwIFn9DSRX3qb_m",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSSRKnC3icvrXSawOVxsUsbbqZ-TjkzB_BodguroXuX15BWKFq8Dw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdGVE5F_KeZpR1BOT-4EEzldPfGgrfpdJeTdRjix6CmT_aB75F",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQgxhI7B0QAcKyZOxX-o84TIoHuB4yrcfliYMMDFVf_xyefPdJe",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDfeZDjXZLa9VoAD4jO3-a7wzZUav2UvndtQCz6mvmQ76cTBQz",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOaY2kvevh0nj-rUTBaTz_cWPFyzTRgD9YkgffJhL7gELMlY1S",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSvzv6EbCcgR0ecllaLk4oVy9v-oM09m0GagA52NrYbs6APfNdL",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxqN8Nw1EejtXE6tMrdj9pFyVauEgWYGVhQKO1-5HW1DvO1iX_",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7vhbvv5vOR90913W-hYeI3vgUsfbBwsCtjQ0NfpQqAjLUhshO",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT1r47wF6H-fCMiHNAXn3-MB5fDfGQ2-RhmA8kmYnowiOL89lfk",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS04RGqynbVEYUGMhAq-NpkjPk00xnYjxZtq1Rog5Bk3nPCQQxngw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS4QssmUJIvIAXxotxTjztU-h3DzV7LL2xGqiGgYfDTeXefmxE5ow",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSS3-78DKOdsi7JZpww1psPZ7pU69aFbOWJ1CCyCbexDJCPtm0t",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhhiQQJilwrbhvHuoK8TgFwrkg9v26FJasnoASIEhlVX_BAY2nxA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjv1e4X3SXTOzE2nXfVdNoM32FGllxedyOp6_KwVQk_OzF2NyOaw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTMYatJkQ6jaHDGRLvUFqJZ21hQlWLrMsclmEH3pYvquLv1vaBKwQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRB_y1yyIz6cQjN9oByHfUJai1ILOj2sLIM261aJiYZUf6gRO-P",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSZeRztAnpDGTLv6GXvSO3DgCV9EIUQm-sEuDCkUBgbgpUQ0Z2ekQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRKcwQlRcqEMI2etVHgY688qUOPxFXeDKbC2ui-iYOnOIVEq5Ma",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnUo4XorE59RlHWWIl_I-3SXgqug6bvpGoMazfh9jS7HhxDWEU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSsWCqDO8hHZ4Scw2sx1KQUKotEJHnH_w1FBSF4e913a0hfyjjg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMP11w8K-IC1AeYKNNZqc-ekL8uObMRyRNvep92bOAkdL61lQEOw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQK4IUUOybQvFXWuCMdhumtUPxrst1yzzyppY9GiaU_f8Zo-moR",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMo5X9KntOsQXflgXi3ASzzbXXkBHiSDOF0hrVPb6kQi-4NGX_",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRptJDm0F5bNXCqLt4KExbB5K9nyF_P3JV7i0rgKDbSJKZ9_wAw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR4PThCYw8HapCkAfdHDAC5mT-AhkS4l4peFgbGSvpso7eQqUMq",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSB7j3gq-i5MDHhnFVXMd1WbLcgT27_Q4UYFnk6iuC1_QOLQAhc",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8OhQWrl66MoLOeftBXGmPksH-z-YbL15xy15CUQ7D8SvdNcTRDw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSXAJvHjKf1-RkOdCW1FhsNYodKXx8HJf9RPtXtiYdI2NYiV3W34A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTVjoQNziDpZdJBTqgOYPdmh1I8FonT3ECFWj-vme-vThdytuV",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTablnRasVov_TD8UnSqCBoSJZWClqJwUxiAFsINW1x9jpLOK3T6A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR657Ios3OSmIkqrWkKaJ7q7zkwlW6qWIlvQMPwNiQKeNHiZBlZKA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1ZnxWDPJOjNhf8wwUb5ZdGNwrtJ5N1Z2z4pyYYXZpZt6d-N4d",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQUuIVu0s7xY6dniRAQQXIbw0TMdcJOawBR_FEeKeZJbgLhaqfR1g",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnCu0unEZnILhqt4boYBnlylgdsheGh5wqFlTpmVFXut86ttYPOA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT2m7XSOQw2wK4B3T97R9rpnEL8XCIkXp0VvxTYOMrMkaGhrQoo",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8iF09XCxcw22mI8j3brhhAPfI_LDdF6QKdh1rauy-5NVV8hxA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSKRQ4dO52Bwmfg_bb6Aql8-Kwyiyn3Wu02DWrNzvAOZzqHo3Kx",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQBBjfuqxBSxefhVACKj7rS1l6fjybVzVA71nL1sfjhAKbxmLeRlQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkA3BC6H8ZJah96QkmkWcaHqcr7wXWLtjWAMmLBPGLtn0Dhx0z",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVZ0aFZMGJd2BomxLIMNwhaIpH2NENGWeQYMK1S0NjxthUSbdSwA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS_NsZP7BMRZKW0EtAUAMgThG27C3fELaP-PV2siRsK7JWOKkCPdQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSLWv1SLDNZCfbhfISbonUqKVOhLQt51_8LNjhH1JR0KUYvH-xR9w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQYkWQXql95eODs7mnx7CaZSL-2c0EABGIOVV5k44fMW2MTZrRCHg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTqsWCYsBc4E9LOLQehgG-m29x0tHGr0eBN8ukJ3iJ1R1YcBS30XQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcReZeMLdFfVbcYL90-i9MD1wK1hR0E56_WWaKrC7FIdCF10AUQEqQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTlsPvwXzm8QV8kA88Q8zP-c3QReMPmHBCPBYamxDYfwv1HV-3K_Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRSSJ-EiH8n_XSE0hvNv8ICRAvRWWUYEiOOYJVvo8Z2NB64wMPj",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUDwCvOfVH0YKeZRQkSZifAMFKy6ynMQw8eC5_n9I4yyUkdLreIA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8lOjE5wDBNx7-_RvLzN2Demhfj4Pazl4eqM_Sf3TGlqzuEATwXg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQC1EMSdHd2YTuODahgAthJ9ZVBUsCcEzVqT20Auw2BXyK3SLlsCw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS-poHYMugAf2MbQVG_VFQ6PUCrd4sqKG934kLi34D1OxOESeJong",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8KAdd0RFcJyPI-k0sjaVVZ-pVL69H0PSMqbKWcD2HD_PwuGbN",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTCa6hgL2GMehJ93ek4ZNNB2v42IKFMsT5gmKL7jO1-0YDOyWYD",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgz3I7qpwU69RJQ4tP7mi39bUST2RFQ3bby62I1iqynHfUBudVGw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2msSsaOkneGZkkhTT0WLOijElADPwEogF7NCfMe8Ggaba45wMIg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPnek1zyvzwVjjrurnBNLMqtOH61LYkVWboE1Lx2aHu4pJuMO-",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkxutUt3MG7hqSUWj-UYEhDJ1rS7zIJ4qVWiTNfNNppXo4Ain6aA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjci4LGWDKHNeCXfgZkgzbMtmVlfLzxe1hITLE0cLJNdujWWd0",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTy9KDtV0C-vWXwC-OWJSjhfV4SEVGQUmwTPYBzw_QUYOR8hiAFwQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAgOTR-wLRmp6Hkq-rLTox_Ge4cZTlzUswO0C8vo9PE9G9QuNbZA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTepmdYKUiJD90QSypA0ce7Kc_N2DRXgDQ5NxjsbNMXboPo3fmZ3w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScr1JIhlLR8fs55MON_X2Uvg2mVLt2T4ML6jBtYRCR4RNqoQ0y",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXIaz_g2PJqX_p5-4S8Oxrvc-bb_6y_i0LxF2NaMEpNkTd8n54",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTM4EB8lezVgHcdI5J1QpKWkDGKcByCFYCPx3qcYi-KgKmmeLLF",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHMBFpgVIYn3V_S-SRDjIl5F_i4nU_kKa1fmNgxS_GvU7wc_zN",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRPUh1zrfUh0kiMZjS-BnQjkmYtFOZdt_IwFFIln7ooR5wPwjZuqA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2SleIBxgszcQioLhbQLRFa-PJYdbgcpxbNjlxdvgv4bZH7A3g",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDABVPJEZx8wjNWi6R6bIkITHeWscQjLHKKYUDgThAi73JACDV",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQk3jr5_VSQFXpdo1J_1u-kqTIBBmx7LzFYeMnskX_Sev5TwS5QWw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTVPFgSi1YDeYHP73XJSdTYI5WkJK-I8eW1kDou7M2XKNcDJjyX",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRGpYYjDa6C3cJAmJ-Snij9uE-Ky0nuosn2uHLiH77Tz-iveZNa",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvDKgW9omuJBKCCYPEMfpU2SCttfLHF6LyOHXOYZOhbmOkioeSsQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScCzpJgzJGzjJrJGRTdgwYCWuU9bEj5NbwHKY2kyFULDxqSSP3",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRgEAMMDlKCB8QIAzXGTAikenKrC9m2azE3Lh6BuSSaK_KBk03i",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDR_Wnul5MjiZdMe4x_G_-cStSmtrbf_rGq1U-YxiU1MZOCAZ08w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR5mfDk7fFTpG08xYxsPOSc9yEFr7g43DYzib9QXiEhqIQ5TEhWjQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkT7rG1kgDcxDGOJMWYetV8a5cssWhE5J6M107NDRLg5OmJeCO3w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqKDJyQ9AmFNkgegNOTfL7CkL1wTaKeK_ZY2zMXoPYX4ozO4M5fQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTWJ6UCpD4hiUggpuMcihg4eeTMTSLpKpxBE_lR8aXKsm0BQ3xfsg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSANWnj5OYUUehiD4QcyX4dxmhPIRs_EbwhZY3u0911KnmSoFoZXQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT71gATPaL6jA4DULNXtCIub64tZa3xfG_guKKXwK1CahVGCu2j",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT6A_I8hu8ddBgzHjrT6sqRFxOa1aI1PJ2XOTlng55itBleqYLrgQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRKw3P5hsdQ60UwgGeXWCpjvkzQyHy_zsZORbQ05duZS0ULBb6m",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ2_mGjxOe2NemH6kC0EMbdTcAbtG4mzNe7duGEmP04YFiScM5s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTczv_iu1v7-iqEEHIEB8v4NhceN4DOBt3NEfrx2FuZd6gDRM-k",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ089kNJEJSIdDHxHrIieNUfB5dvlWjrKAr6JoPJL6ERQQ2Idpn",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQTHQJPZX13uFZaP9kLqi2PzP3GyZm5aIJ8rtxKB6r5kgl8VfAuMA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0f0PjpOrQKu-uENQryrqICnyzx6N00n5USgodgS_U8YznSNvK",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSeRFF2u9WzyHsYlQYfv7Jtz7ONG6JDkuku7czKDrjT_lYow33Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRjxs_4n3MnaKo70d0UF-jYgseOjWHpw-9RtDLuff_8ymsXqSuitA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8X9WBcNNrwJZOLKZpK3GdrTT5XHMzh5NnQdTaBz7k7KfkkeWz4g",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSd8h78HO7ralZf4N3Bl928vMsbCQbmkeFEFK2cMvWCLt09ftjDbw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTdDV-U0FMLeJNGzxJT27al8FSLKvVzlsY3Lg70SNolJmOTN8JpCQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS8K2kgPgtQt0arfDgykOEl60NEXFzrinYXSIEdOFQDvNdb1Va5qA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRDuD5bv6W1O-gQQunjZDIa8xx3ubll7hF-_JCiC6-xPxIx6QCs",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTJkVrnW9W2pzVPrIW2VRqGOR6WEiTVAvWBW26ndgbVRjFP8fAn",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTgr00niSkAWDUCpgGxhIH_DLdCekJIme5F2D1t1uhyXriaY7uq",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR324zZvuHIPzpNTj81EVl6ea_pfcLCyLW80a_YZq6_ua7DfJHw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTXEg6BWNKdBJOpgWahCthuqc6BhmfXIjD1d89JtQPg-ufEQ3b5yg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDFQidPX6Yxq_tsj6EDkx-lNh0j7SBUUA6hmaZqWjT3W7T-2zg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7srIn1KPMujwg_XvaaBNwuOMKWyXrDlAcEmCEUos_CYRHDFF2pw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ0rzp4L1lyFuka0jCwId73nuktPaOeDtC8vGMdwW937vih7OIOFA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQM44ueJHPQUh0DqOuXBToxdlYcoNT9NjlX4N_p_96JqueiiD2s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQqsRge63gWbr_O0xMfojXU91gwddoR7exf5RFfcPgMJAHMci8A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMMuFGpemn7FXgiQDJpP_r0a3gxE7lorP7H2GcqGDNWeOyn6qB_A",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8z8HzBG0i20GIxFL-SPZeGas9XZlbNaVOL3sUsCNkwfAwV028",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkJ-5RsloPHuQb1p7UcELkJ7tB68P_sqnwf9qnzmHne-uV4vUyyg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSxzFpzBTJdzPOT6Ho41RyjnnGumco42ARNBSJ-krTudHmLR_UT",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcROnsPE-sxLY-ZIkDWGDHDmS_BZ8FdZOJAgLiIgxGty6EIeuSIU",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRydAGoQM0AfNxKf-9qlD1dkmiO7CdmdelpMyHEAxL_hxHJaIe6Iw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4RpvB75RBRAEmeHBVMVsDYuXRFbnijMzH6dSJ1rstYMUNUkVz",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlu2H7kUxJqVIe1s1EidA1GbuGk2yIFOSQTo-sh7V52AXNw0ub",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSarNu46W8N_HAoDXmdN9SSKEgLayTIf07y4qBYzWY-DrlrRn7Mjg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRxdj61vD_NpXATXBR3VpNaKozRAG330BKSle5SIWFG7Vmh3PieOw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQK9OTqzHHgKMOwUUDNZ-b-ei87sQYq0ue-TgBL5aPfxYphZ0gh",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS5BNkvWQdRh_2DxUaDen-aQ3SF39oHzDKxuibODd25vRJx4CsVDg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQV55dr2zgH3FXkEirCsfI-hgGF5-W-Hth5HTHB7clM9j4VQ_1M",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTk12MGMTkxSolKn2UKfPat-9xpP5piAiT_QVTzAJIW8CQkMi3d",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMM1tQahIdUPa9ZTg9CPySZsZbkRuOhvxD8QP2pC7gPxKRAn7AyA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8U9PqrE1nd872agZq5ck1Ng_3-QKrH9qrDbxXY9domSUH4Fu6bQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ8YrZ2VAyeAB0yL_pVR1BESB-Hkq3ekLqivc8msterkaRzrbT7VQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQOY3W2WD44EbruiNBqQHBLJaPRemDC0DXFiwcm5ABL06xVB0Oc",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT0unW8qcs_NiRk4lwJzlJ2bw9AeUqgx5dktG1lwMHwCAJ6iQjs",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTIbRQiAu4Icj_Cyafl8jyiyXtqs7jLU-celfh6NEGitDj1e9Bu",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScBWmo7G-xlyK3C_FjMbR2P0dotUcvVLoBtae36H2NjN2D2vce0g",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS5vEvwbvHZogE8J0fafbUwONUP4kGJ38BBR2izSZGi5r1OBrIrig",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsgciEudRYju2k8xSrd6b0NfXHnoruxqOkVCYk3c7Mc2JxWcCI",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvE_M_ZXp8C6UZuXrwqciD0wuFi0K-Fz_jWRHdt3fi7_Jzy7n34Q",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR_W0SEHMRWcn8MkbjgRRh2F1dNVa7KcdNxlO9fOZOcbS7J51iK",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcThYI3rtH4pmHEYPEEQcU6C6Iamgahzwo3aqkG279JJ8q_kAhsY",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQdrjbtdPVhgk0iPg2uM4lSAvyyzusm7TZL57CQcgYnXg8Tq1iUBw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRA0tEr3DxBmP9f-UgZSXv25fpFAmI2bBalcq6ywEqVtSGk_qrxTg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQv6r7OoCC7UapXiUeEguUhM450h82-BFcdqszl4hghK-WIToRN",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRFjqFY9cpLTxwKSOfCrLRcVscqbS9nF-VKxGQd8iB0QzVkEJoo",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQmkew4BFfxMSCIupwVNTFjbrG9y0UHPi5uEAfTlW_w_yRBlLdXyw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRbQQT_8BkGL0ENssemtlEgx7K06FUzRZTElfKDQvFH_vg0GvbS",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTtGBv3cKj1IVbzhFAmCz6ifUDnaF9PEY5PVIH-GEil3goOM4pK",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQxHlFWYbHeCg3xp5_sDaZyjoLEqDTUwBSnAozqjVHAYj1Gk-f-",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRBmw66QaV4FuavOU42D4SJGCLHyhysMH8lhPORz9aXiyN60PR7cg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTuQKVuGe1TszP53oNbgzpYU-XGK5fILGShHizbHaF_ACD6BGVg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSMFYuvJOPbCQhX8FT7Y3HL9qjDH66YryIUYagdegUMN-Dzaq6yXg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2N6857inPRxBhkPFE2NpCWFUPQoQ9NjtjdVqYgv4nnxN4CHMRtw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRmukskUJO372TElkZmymI8GiV-1sYo9vt9-ADJFJF-5ymlzMYrwA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRlRYS4HyLZ4CV2mxDm2oU46UqKhVXY_zvjPXjKwojY8I1IUrP1",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRTU5JyTQZf0QutFi1elDl6c2ljJJcd94Cc2tyGebcug1ZcoAur",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFUQannRged5caAsqxWMxVi2DQr21hpxxweAeo4UKmzuS5Fci4",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQFpXsw-y18JUXaxu2C8LYe6DRgixjJjXcVbUCLzXhwWEj5mtsG",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSJTimo55YZizKRtp2iqVGzzVRJ8C5M2B_50-QWww6Sz8kLrry3cg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSjuD1gzSI1QwkiwwWjY8kjNpMmAfVWxrJoJkITtagUd0tkx0MT",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKe4hnNcWUX2OU1fdW3XgqfUKUQX_v7xNSP3yR7Q2YwDR7s8GAbA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQn9hqk5FaFQMyTfXOXzvnLCC9hrFTmWQViZEJwkxHkI7JMvdVoaA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3BNJOtGznmLYgRcT_0RUvMf2B-h99rn9KjuzQ6zLN7ovndZYo_g",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRYbVnHrpifWx5MRecManRO58-mvWNsDyMuMbYKdj9Rlaj_VBvo",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSvouGAOoy37M9VkbA7V3PGBQJLz78RkQ9DygU1iJquJ9plU_rsw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS0yp3ZKT0Tq2J2nNeuArNe2XjAfkXNEABK1AEYHD_KN5hwaEjmaA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRoFzjBjDm8ymdvk0r9hz87Ue7bcuT5jNGLboAIlOWCN28YM3f1",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4N6-Q-6RBn1lo-sPd8Qbeu8seInGJsH7HjqDlodsIcr8ZuiXl",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQSa5DjT2YUCkZMuTxKq0LqKKpqk3YddrCPBFTnORbrOqSYgVM5cg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRKE8xc67CtNXnj24i1j49PZw4gv8X7GNa5Z1vkPu1Nw04_iGIu",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTBWp8UZa-bBZKroBwjm-meiTso3b3r9RTQpBcWkcfRcNE0NFsq",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQvjtWpv88gzp9H5r5bI66BnOzh7zxmNrQIyyZl31GrMogCkLSWdg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTIx58NaK9BWG7e8AW0tTpihmv7qM-N7Yk3Llb-SPWt96KuR6Dt",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQd8vuVOBdfTl70BshNR4Ti7pHqchEytYBCXwlYjt0Lj0O0g2vRRg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_Ou1EcPfiN7MpNLqQRkPZQe2Ykg5Oiw4hrhBJTmkKXvihbx8a",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS24LN6sgySRP6EhHWnOH7CGc6SRARoUsZgK7ZqmvzBqcsThIAD",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ1qZ8pZerfvw8OxtbChkZHASzta7Xc5Eu_zww7oUFrY5X2x_rc",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRJQ6IaLdkNDe5FQ4HyF31vLf9LfVePJvKqsXNX-tP2zPob2wtomQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRV4TmXEqCsFpaNKraRfsbgkUt87531KfjbjGZXKXmFGdl_Join3w",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsTOIu7Zs-Fsnc8M4pzSxc_d75Zf5aDWDPDPYvyNP58X23LjdCDQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcCqYKb5x5Mpx_ILHmoYGkgX10ZQD4js4MH6xXLr44mMxsSW3T",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTl6eGOloPXcGpFrohg-uJdTrcykvL0qj5dhVzfnNZF-CSgnf3nXA",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTDaE_oBamS-3VqsorOFA5UWVkVvpauMEKH0wwQAkJsxn-GXdNK",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR2bxSCLSudSJ7sPsmsUYXtoB4c9NDY16E3dFsJYLjq8YSZxaqp",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRMK0TpU9d52fb4BGnVFYrHgDDz6c4CXKTA93DPjT5bhv9vCWOS",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRkEpsYMngQ32wSXMF5JQkcbWmYgOUrbqx7plPFAI9SSB1CbqOr",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQdF_pFSOYJ_BlAtlRe3x8QquY2ozBAYLdyVzqxPaOvJv-vZY2RPw",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRyVIO0BfntlFPQJ-Aph7UirR5ZtS_nvDNRyrxis9V3AcODVO066g",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTCLrV1guYnnaHCcEYA3pEXSQ8lTyvKYYGXQiEcfCQoK6AQowO_lQ",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQE-gGeFstA8ieCfl5p3gO-5eqyQTZ8ztuhovQgDFnyvQ-X-lVDXA",
                "https://www.gstatic.com/kpui/social/fb_32x32.png",
                "https://www.gstatic.com/kpui/social/twitter_32x32.png",
                "https://www.gstatic.com/kpui/social/gplus_32x32.png",
                "https://www.google.co.za/images/icons/material/system/1x/email_grey600_24dp.png",
        };
        return images[seed % images.length];
    }

    private HashMap<String, ArrayList<Media>> getAlbumMedia() {
        if (albumMediaMap == null) {
            albumMediaMap = new HashMap<String, ArrayList<Media>>() {{
                for (int n = -2; n >= -1 * ALBUM_COUNT; n--) {
                    ArrayList<Media> media = new ArrayList<Media>();
                    for (int i = 0; i < MEDIA_COUNT; i++) {
                        Media.Builder m = Media.newBuilder(String.valueOf(n))
                                .setId(i)
//                                .setDisplayName(String.valueOf(i))
                                .setMimeType("image/jpeg")
                                .setUri(getImage(i))
                                .setDateTaken(System.currentTimeMillis() - (int) (Math.random() * 100000));
//                        if (n < -1 * (ALBUM_COUNT / 2)) {
//                            m.setDisplayName(null);
//                        }
                        media.add(m.build());
                    }
                    Collections.sort(media, new Comparator<Media>() {
                        @Override
                        public int compare(Media o1, Media o2) {
                            return Double.compare(o2.getDateTaken(), o1.getDateTaken());
                        }
                    });
                    put(String.valueOf(n), media);
                }
            }};
        }
        return albumMediaMap;
    }
}
