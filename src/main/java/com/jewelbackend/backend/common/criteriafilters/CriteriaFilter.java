package com.jewelbackend.backend.common.criteriafilters;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.Type;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CriteriaFilter<E> {

    public List<E> getEntitiesByCriteria(Class<E> entityClass, Map<String, String> map, EntityManager entityManager)
            throws ParseException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = applyQuery(entityClass, map, entityManager, criteriaBuilder);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public List<E> getEntitiesByCriteria(Class<E> entityClass, Map<String, String> map, EntityManager entityManager,
                                         int size, int pageNumber)
            throws ParseException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = applyQuery(entityClass, map, entityManager, criteriaBuilder);
        int firstResult = (pageNumber) * size;
        return entityManager.createQuery(criteriaQuery)
                .setFirstResult(firstResult)
                .setMaxResults(size).getResultList();


    }

    public List<E> getEntitiesByCriteriaWithSorting(Class<E> entityClass, Map<String, String> map, EntityManager entityManager,
                                                    String sortingColumn)
            throws ParseException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = applyQuery(entityClass, map, entityManager, criteriaBuilder);
        return entityManager.createQuery(criteriaQuery).getResultList();


    }

    public long getQueryCount(Class<E> entityClass, Map<String, String> map, EntityManager entityManager) throws ParseException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = applyQueryForCount(entityClass, map, entityManager, criteriaBuilder);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }


    private CriteriaQuery<E> applyQuery(Class<?> entityClass, Map<String, String> map, EntityManager entityManager, CriteriaBuilder criteriaBuilder) throws ParseException {
        CriteriaQuery<E> criteriaQuery = (CriteriaQuery<E>) criteriaBuilder.createQuery(entityClass);
        Root<E> root = (Root<E>) criteriaQuery.from(entityClass);
        List<Predicate> orPredicates = new ArrayList<>();
        List<Predicate> andPredicates = new ArrayList<>();
        for (var map1 : map.entrySet()) {
            Class<?> javaType = null;
            if (map1.getKey().contains(".")) {
                var keys = map1.getKey().split("\\.");
                CriteriaFilter<?> javaTypeCriteriaFilter = new CriteriaFilter<>();
                Join<E, ?> join = root.join(keys[0], JoinType.INNER);
                orPredicates.add(criteriaBuilder.equal(join.get(keys[1]), map1.getValue()));
            } else {
                javaType = getFieldType(entityClass, map1.getKey(), entityManager).getJavaType();
                if (javaType == String.class) {
                    orPredicates.add(criteriaBuilder.like(root.get(map1.getKey()), "%" + map1.getValue() + "%"));
                } else if (javaType == Date.class || javaType == Timestamp.class) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    if (map1.getKey().contains("<")) {
                        var value = map1.getValue();
                        Date date = simpleDateFormat.parse(value);
                        andPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(map1.getKey().substring(0, map1.getKey().length() - 1)), date));
                    } else if (map1.getKey().contains(">")) {
                        var value = map1.getValue();
                        Date date = simpleDateFormat.parse(value);
                        andPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(map1.getKey().substring(0, map1.getKey().length() - 1)), date));
                    } else {
                        Date date = simpleDateFormat.parse(map1.getValue());
                        orPredicates.add(criteriaBuilder.equal(root.get(map1.getKey()), date));
                    }
                }

            }

        }
        Predicate combinedPredicate = null;
        if (!orPredicates.isEmpty() && !andPredicates.isEmpty()) {
            combinedPredicate = criteriaBuilder.and(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])), criteriaBuilder.and(andPredicates.toArray(new Predicate[0])));
        } else if (!orPredicates.isEmpty()) {
            combinedPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
        } else if (!andPredicates.isEmpty()) {
            combinedPredicate = criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
        }


        criteriaQuery.where(combinedPredicate);
        criteriaQuery.orderBy(criteriaBuilder.asc(root.get("id")));

        return criteriaQuery;
    }


    public List<E> getEntitiesByCriteriaForSearch(Class<?> entityClass, Map<String, String> map, EntityManager entityManager,
                                                  int size, int pageNumber, List<Predicate> predicates)
            throws ParseException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> criteriaQuery = applyQuery(entityClass, map, entityManager, criteriaBuilder);
        var count = entityManager.createQuery(applyQueryForCount(entityClass, map, entityManager, criteriaBuilder)).getSingleResult();

        if ((long) pageNumber * size > count) {

            // Add your own sorting logic here
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(0)
                    .setMaxResults(size).getResultList();
        } else {
            return entityManager.createQuery(criteriaQuery)
                    .setFirstResult(pageNumber * size)
                    .setMaxResults(size).getResultList();

        }

    }

    public Type<?> getFieldType(Class<?> entityClass, String fieldName, EntityManager entityManager) {

        EntityType<E> entityType = (EntityType<E>) entityManager.getMetamodel().entity(entityClass);

        if (fieldName.contains("<") || fieldName.contains(">")) {
            fieldName = fieldName.substring(0, fieldName.length() - 1);
        }
        SingularAttribute<? super E, ?> attribute = entityType.getSingularAttribute(fieldName);

        return attribute.getType();

    }


    CriteriaQuery<Long> applyQueryForCount(Class<?> entityClass, Map<String, String> map, EntityManager entityManager, CriteriaBuilder criteriaBuilder) throws ParseException {
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<E> root = (Root<E>) criteriaQuery.from(entityClass);
        criteriaQuery.select(criteriaBuilder.count(root));
        List<Predicate> orPredicates = new ArrayList<>();
        List<Predicate> andPredicates = new ArrayList<>();
        for (var map1 : map.entrySet()) {
            Class<?> javaType = null;
            if (map1.getKey().contains(".")) {
                var keys = map1.getKey().split("\\.");
                CriteriaFilter<?> javaTypeCriteriaFilter = new CriteriaFilter<>();
                Join<E, ?> join = root.join(keys[0], JoinType.INNER);
                orPredicates.add(criteriaBuilder.equal(join.get(keys[1]), map1.getValue()));
            } else {
                javaType = getFieldType(entityClass, map1.getKey(), entityManager).getJavaType();
                if (javaType == String.class) {
                    orPredicates.add(criteriaBuilder.like(root.get(map1.getKey()), "%" + map1.getValue() + "%"));
                } else if (javaType == Date.class || javaType == Timestamp.class) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    if (map1.getKey().contains("<")) {
                        var value = map1.getValue();
                        Date date = simpleDateFormat.parse(value);
                        andPredicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(map1.getKey().substring(0, map1.getKey().length() - 1)), date));
                    } else if (map1.getKey().contains(">")) {
                        var value = map1.getValue();
                        Date date = simpleDateFormat.parse(value);
                        andPredicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(map1.getKey().substring(0, map1.getKey().length() - 1)), date));
                    } else {
                        Date date = simpleDateFormat.parse(map1.getValue());
                        orPredicates.add(criteriaBuilder.equal(root.get(map1.getKey()), date));
                    }
                }

            }

        }
        Predicate combinedPredicate = null;
        if (!orPredicates.isEmpty() && !andPredicates.isEmpty()) {
            combinedPredicate = criteriaBuilder.and(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])), criteriaBuilder.and(andPredicates.toArray(new Predicate[0])));
        } else if (!orPredicates.isEmpty()) {
            combinedPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
        } else if (!andPredicates.isEmpty()) {
            combinedPredicate = criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
        }


        criteriaQuery.where(combinedPredicate);

        return criteriaQuery;
    }

}
