package DeliveryContract;

import java.io.Serializable;

@FunctionalInterface
public interface IDeliveryContract extends Serializable {

  /**
   * Returns an object
   *
   * @return    REturns an object.
   */
    Object get();
}
