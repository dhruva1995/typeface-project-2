import React from "react";
import "./App.css";
import { ActivitiesScreen } from "./components/ActivitiesScreen";
import SelectUser from "./components/SelectUser";

enum PageState {
  USER_NOT_SELECTED,
  FETCHING_DATA,
  DATA_RENDERING,
  ERROR,
}

function App() {
  const [user, setUser] = React.useState<string>();

  const onUserSelect = (userName: string) => {
    setUser(userName);
  };

  if (!user) {
    return <SelectUser onUserSelect={onUserSelect} />;
  } else {
    return <ActivitiesScreen user={user} />;
  }
}

export default App;
