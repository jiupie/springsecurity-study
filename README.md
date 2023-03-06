# spring security学习

> 学习视频地址：https://www.bilibili.com/video/BV1Rv4y1w74n

## spring security构建类

- SecurityBuilder：构造者模式- 构建一个类
- SecurityConfigurer:配置类
- HttpSecurity:生成DefaultSecurityFilterChain
- WebSecurity：构建Filter
- SecurityFilterChain：根据HttpServletRequest判断是否匹配，获取匹配的过滤器链
- FilterChainProxy:也是一个filter，filter的开始执行类，

进入流程：FilterChainProxy(本身也是一个filter)->根据DefaultSecurityFilterChain的matches获取一个过滤器链->执行SecurityFilterChain中的一系列过滤器

## AuthorizationFilter(授权过滤器)

`AuthorizationFilter`这个用户授权的过滤器，还有`FilterSecurityInterceptor`

源码查看顺序：AuthorizationFilter->AuthorizationManager->AuthorizationDecision

### `AuthorizationManager`和`AuthorizationFilter`的构建

通过`AuthorizeHttpRequestsConfigurer`的config方法

## `FilterSecurityInterceptor`过滤器

## 方法级别的鉴权

使用interceptor拦截器进行`MethodSecurityInterceptor`

# 使用

## 配置

如果配置不加上formLogin就不会往过滤器中注入`UsernamePasswordAuthenticationFilter`

```java
httpSecurity.formLogin()
```
