package org.gallery.demo.db.transactions;

import org.gallery.demo.db.connection.DatabaseManager;
import org.gallery.demo.mo.GalleryDetailMO;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class ImageGalleryDao {

    private Jdbi jdbi;
    private DatabaseManager databaseManager;

    public ImageGalleryDao() {
        databaseManager = new DatabaseManager();
        jdbi = databaseManager.getJdbiConnection();
    }

    public void createImageGalleryTable() {
        jdbi.useHandle(handle ->
                handle.execute(DDLQueries.GALLERY_CREATE_DDL)
        );
    }

    public void saveGalleryDetail(GalleryDetailMO galleryDetailMO) {
        jdbi.useHandle(handle -> {
            handle.createUpdate(DMLQueries.GALLERY_INSERT_DDL)
                    .bind("name", galleryDetailMO.getGalleryName())
                    .bind("tags", galleryDetailMO.getGalleryTags()).execute();

        });
    }

    public List<GalleryDetailMO> getAllGalleryDetails() {
        return jdbi.withHandle(handle ->
                handle.createQuery(DMLQueries.GALLERY_SELECT_DDL)
                        .map((rs, ctx) -> new GalleryDetailMO(rs.getInt("id"), rs.getString("name"), rs.getString("tags")))
                        .list());
    }

    public boolean removeGalleryDetail(int id) {
        try {
            jdbi.useHandle(handle -> {
                handle.createUpdate(DMLQueries.GALLERY_DELETE_DDL)
                        .bind("id", id).execute();

            });
        } catch (Exception exception) {
            return false;
        }
        return true;
    }
}
