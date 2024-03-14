package users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.dao.HasId;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable, HasId {

  @Serial
  private static final long serialVersionUID = 1234567L;
  private int id;
  private String username;
  private String password;
  private String email;
  private String avatar;
  private Boolean like;
  private long created_at;

  @Override
  public int getId() {
    return id;
  }
}
