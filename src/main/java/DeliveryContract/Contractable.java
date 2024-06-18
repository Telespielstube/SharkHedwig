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
   * Sets the current created state for a delivery contract component.
   * @param isCreated
   */
  void setIsCreated(boolean isCreated);

  /**
   * Returns the state of the delivery contract and its two components.
   *
   * @return    boolean value true if it is created or false if not.
   */
  boolean isCreated();
}