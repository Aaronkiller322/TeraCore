package me.aaron.TeraCore.Component;


public abstract class Content {
  public String toString() {
    return "Content()";
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof Content))
      return false; 
    Content other = (Content)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof Content;
  }
  
  public int hashCode() {
    int result = 1;
    return 1;
  }
  
}
