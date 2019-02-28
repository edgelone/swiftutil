package cn.tujia.swiftutil.model;

import org.apache.http.HttpResponse;
import org.javaswift.joss.headers.SimpleHeader;

/**
 * @author lk
 * @date 2019/2/26
 */
public class ContainerStoragePolicy extends SimpleHeader {
  private static final String X_CONTAINER_STORAGE_POLICY = "X-Storage-Policy";

  public ContainerStoragePolicy(String value) {
    super(value);
  }

  @Override
  public String getHeaderName() {
    return X_CONTAINER_STORAGE_POLICY;
  }

  public ContainerStoragePolicy fromResponse(HttpResponse response) {
    return new ContainerStoragePolicy(convertResponseHeader(response, X_CONTAINER_STORAGE_POLICY));
  }

}
