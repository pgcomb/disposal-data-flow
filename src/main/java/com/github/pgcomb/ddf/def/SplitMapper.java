package com.github.pgcomb.ddf.def;

import com.github.pgcomb.ddf.map.AbstractPipelineMapper;
import com.github.pgcomb.ddf.map.MapPair;
import com.github.pgcomb.ddf.map.api.Payload;
import com.github.pgcomb.ddf.map.api.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串分割mapper
 *
 * @author 王东旭
 * @deprecated {@link com.github.pgcomb.ddf.map.DefaultMapper}
 */
@Deprecated
public class SplitMapper extends AbstractPipelineMapper<String, Principal, Payload> {

    private static final Logger log = LoggerFactory.getLogger(SplitMapper.class);

    private String separator;

    private static final String NAME = "mapper";

    public SplitMapper(String separator) {
        this.separator = separator;
    }

    @Override
    public MapPair<Principal, Payload> map(String in) {
        String[] split = in.split(separator, 2);
        StringPrincipal stringPrincipal;
        StringPayload stringPayload;
        if (split.length == 0) {
            stringPrincipal = new StringPrincipal("");
            stringPayload = new StringPayload("");
        } else if (split.length == 1) {
            stringPrincipal = new StringPrincipal(split[0]);
            stringPayload = new StringPayload("");
        } else {
            stringPrincipal = new StringPrincipal(split[0]);
            stringPayload = new StringPayload(split[1]);
        }
        MapPair<Principal, Payload> mss = new MapPair<>(stringPrincipal, stringPayload,separator);
        log.debug("input[{}],mapper[{}]", in, mss);
        return mss;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
