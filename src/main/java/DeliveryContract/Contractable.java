package DeliveryContract;

import java.io.Serializable;

@FunctionalInterface
public interface Contractable extends Serializable {

  /**
   * Returns an object
   *
   * @return    REturns an object.
   */
    Object get();
}
