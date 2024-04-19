package DeliveryContract;

import java.io.Serializable;

public interface Contractable extends Serializable {

  /**
   * Returns an object
   *
   * @return    Returns an object.
   */
  Object get();

  /**
   * Returns a boolean value of the DeliveryContract component state if it is already created or not.
   *
   * @return    Boolean value of component state.
   */
  boolean getIsCreated();
}
