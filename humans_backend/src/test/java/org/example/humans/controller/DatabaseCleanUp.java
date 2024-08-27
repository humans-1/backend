package org.example.humans.controller;

import com.google.common.base.CaseFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.introspection.CaseFormatUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseCleanUp implements InitializingBean {

    private final EntityManager entityManager;
    private List<String> tableNames = new ArrayList<>();
    private List<String> excludedTables = Arrays.asList("note"); // 제외할 테이블 목록

    @Override
    public void afterPropertiesSet() throws Exception {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
                .map(entityType -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityType.getJavaType().getSimpleName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void truncateAllEntity() {
        entityManager.flush();

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (String tableName : tableNames) {
            // 제외할 테이블 목록 확인
            if (!excludedTables.contains(tableName)) {
                try {
                    entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
                } catch (Exception e) {
                    log.warn("Table {} not found or cannot be truncated: {}", tableName, e.getMessage());
                }
            }
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }
}
