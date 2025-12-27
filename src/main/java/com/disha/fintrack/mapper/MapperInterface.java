package com.disha.fintrack.mapper;

public interface MapperInterface<D, E> {
    D toDto(E entity);
    E toEntity(D dto);
}
