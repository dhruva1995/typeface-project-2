import {
  Avatar,
  Container,
  FormControl,
  InputLabel,
  ListItem,
  MenuItem,
  Select,
  SelectChangeEvent,
  Typography,
} from "@mui/material";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import React from "react";
import classes from "./SelectUser.module.css";

export type SelectUserInput = {
  onUserSelect: (user: string) => void;
};

const SelectUser: React.FC<SelectUserInput> = (props: SelectUserInput) => {
  const userChangeHandler = (event: SelectChangeEvent<string>) => {
    props.onUserSelect(event.target.value);
  };

  const users = [...Array(100).keys()]
    .map((int) => `user-${int}`)
    .map((username) => (
      <MenuItem
        value={username}
        className={classes.userItem}
        divider
        key={username}
      >
        <ListItem>
          <ListItemIcon>
            <Avatar
              alt={username}
              src={require(`./../assets/images/${Math.floor(
                Math.random() * 4
              )}.jpg`)}
              sx={{ width: 24, height: 24 }}
            />
          </ListItemIcon>
          <ListItemText>{username}</ListItemText>
        </ListItem>
      </MenuItem>
    ));

  return (
    <Container>
      <Typography variant="h4" className={classes.title}>
        No Login required, just impersonate as user.
      </Typography>
      <div className={classes.selectionPane}>
        <FormControl fullWidth>
          <InputLabel id="user-select-label">User</InputLabel>
          <Select
            labelId="user-select-label"
            id="user-select"
            label="User"
            placeholder="Select a User"
            onChange={userChangeHandler}
          >
            {users}
          </Select>
        </FormControl>
      </div>
    </Container>
  );
};

export default SelectUser;
