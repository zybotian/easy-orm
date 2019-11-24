package org.oasis.easy.orm.conf.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public class Signer {
    private String id;
    private String type;
    private String title;
    private String image;
}
