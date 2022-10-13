package com.donation.domain.entites;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDonation is a Querydsl query type for Donation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDonation extends EntityPathBase<Donation> {

    private static final long serialVersionUID = 1427268663L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDonation donation = new QDonation("donation");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createAt = _super.createAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QPost post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateAd = _super.updateAd;

    public final QUser user;

    public QDonation(String variable) {
        this(Donation.class, forVariable(variable), INITS);
    }

    public QDonation(Path<? extends Donation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDonation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDonation(PathMetadata metadata, PathInits inits) {
        this(Donation.class, metadata, inits);
    }

    public QDonation(Class<? extends Donation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

