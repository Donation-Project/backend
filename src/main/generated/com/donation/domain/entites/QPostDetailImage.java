package com.donation.domain.entites;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostDetailImage is a Querydsl query type for PostDetailImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostDetailImage extends EntityPathBase<PostDetailImage> {

    private static final long serialVersionUID = 1013851781L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostDetailImage postDetailImage = new QPostDetailImage("postDetailImage");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final QPost post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAd = _super.updateAd;

    public QPostDetailImage(String variable) {
        this(PostDetailImage.class, forVariable(variable), INITS);
    }

    public QPostDetailImage(Path<? extends PostDetailImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostDetailImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostDetailImage(PathMetadata metadata, PathInits inits) {
        this(PostDetailImage.class, metadata, inits);
    }

    public QPostDetailImage(Class<? extends PostDetailImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
    }

}

