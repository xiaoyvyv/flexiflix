cmake_minimum_required(VERSION 3.22.1)

project("libjs.so")

include_directories(libnode/include/node)

# 构建动态库
add_executable(${CMAKE_PROJECT_NAME}
        node-extension.cpp
)


add_library(libnode
        SHARED
        IMPORTED)

# 需要从 https://github.com/nodejs-mobile/nodejs-mobile/releases 下载二进制文件
# 根据架构放置放到 libnode/bin/{abi}/libnode.so 路径下
set_target_properties(
        libnode
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libnode/bin/${ANDROID_ABI}/libnode.so)

target_link_libraries(
        ${CMAKE_PROJECT_NAME}
        libnode
        log)