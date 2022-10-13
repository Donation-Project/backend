package com.donation.domain.embed;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWrite is a Querydsl query type for Write
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QWrite extends BeanPath<Write> {

    private static final long serialVersionUID = 1317750575L;

    public static final QWrite write = new QWrite("write");

    public final StringPath content = createString("content");

    public final StringPath title = createString("title");

    public QWrite(String variable) {
        super(Write.class, forVariable(variable));
    }

    public QWrite(Path<? extends Write> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWrite(PathMetadata metadata) {
        super(Write.class, metadata);
    }

}

