package swp.happyprogramming.utility;

public interface Transformer<K, V> {

  V transform(K object);

}
