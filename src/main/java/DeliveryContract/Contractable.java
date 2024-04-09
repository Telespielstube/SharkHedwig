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
   * Returns a boolean value if the component is already created.
   *
   * @return    Boolean value of component state.
   */
  boolean getIsCreated();
}
