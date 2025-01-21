package com.alura.Literatura.service;

public interface IConvertData {
    <T> T getData(String json, Class<T> clase);
}
