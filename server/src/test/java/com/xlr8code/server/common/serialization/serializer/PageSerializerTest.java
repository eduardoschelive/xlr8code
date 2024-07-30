package com.xlr8code.server.common.serialization.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class PageSerializerTest {

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        // Make this accept PageSerializer<T> instead of PageSerializer
        module.addSerializer(PageImpl.class, new PageSerializer());
        mapper.registerModule(module);
    }

    @Test
    void it_should_serialize() throws IOException {
        List<String> content = List.of("item1", "item2", "item3");
        PageRequest pageRequest = PageRequest.of(1, 3, Sort.by("property").ascending());
        Page<String> page = new PageImpl<>(content, pageRequest, 10);

        String json = mapper.writeValueAsString(page);

        String expectedJson = "{" +
                "\"content\":[\"item1\",\"item2\",\"item3\"]," +
                "\"pagination\":{" +
                "\"pageSize\":3," +
                "\"pageNumber\":2," +
                "\"first\":false," +
                "\"last\":false," +
                "\"empty\":false," +
                "\"hasNext\":true," +
                "\"hasPrevious\":true," +
                "\"totalPages\":4," +
                "\"totalElements\":10," +
                "\"numberOfElements\":3" +
                "}," +
                "\"sort\":[{" +
                "\"property\":\"property\"," +
                "\"direction\":\"ASC\"" +
                "}]" +
                "}";

        assertEquals(expectedJson, json);
    }
}
