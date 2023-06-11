import NotificationsIcon from "@mui/icons-material/Notifications";
import {
  AppBar,
  Badge,
  Button,
  Divider,
  Grid,
  IconButton,
  Toolbar,
  Typography,
} from "@mui/material";
import React from "react";
import useWebSocket from "react-use-websocket";
import Activity from "../models/Activity";
import classes from "./ActivitiesScreen.module.css";
import ActivityList from "./ActivityList";
import LoadingScreen from "./LoadingScreen";
export const ActivitiesScreen: React.FC<{ user: string }> = ({ user }) => {
  const [feed, setFeed] = React.useState<Activity[]>([]);
  const [notifications, setNotifications] = React.useState<Activity[]>([]);
  const [dataFetched, setDataFetched] = React.useState(false);
  const [error, setError] = React.useState<Error>();

  const { lastMessage } = useWebSocket(
    `ws://localhost:8082/notifications/${user}`,
    {
      onOpen: () => {
        console.log("Successfully opened web socket connection to server!");
      },
      onError(event) {
        console.log(`Error on webocket ${event}`);
        setError(new Error(`Error on web socket ${event}`));
      },

      reconnectAttempts: 10,

      reconnectInterval: (attemptNumber) =>
        Math.min(Math.pow(2, attemptNumber) * 1000, 10000),
    }
  );

  React.useEffect(() => {
    if (lastMessage?.data) {
      const newActivity = JSON.parse(lastMessage.data) as Activity;
      setNotifications((oldNotifications) => [
        newActivity,
        ...oldNotifications,
      ]);
    }
  }, [lastMessage]);

  React.useEffect(() => {
    const reset = async () => {
      setFeed([]);
      setNotifications([]);
      setDataFetched(false);
      try {
        const response = await fetch(
          `http://localhost:8081/api/v1/feed/${user}`
        );
        if (response.ok) {
          const result = await response.json();
          setFeed(result);
          setDataFetched(true);
        } else {
          throw new Error(
            `Failed fetching response: status-text=${response.statusText}`
          );
        }
      } catch (e) {
        setDataFetched(true);
        console.log(e);
        setError(e as Error);
      }
    };

    reset();
  }, [user]);

  const [showNotifications, setShowNotifications] = React.useState(false);

  const toggleNotification = () => {
    setShowNotifications((old) => !old);
  };

  const mergeNotificationsIntoFeed = () => {
    setFeed((old) => [...notifications, ...old]);
    setNotifications([]);
  };

  const notificationContent = (
    <>
      <Divider orientation="vertical" flexItem>
        <></>
      </Divider>
      <Grid item xs>
        <Typography variant="h4" textAlign="center" marginBottom={"0.5rem"}>
          Notifications
        </Typography>
        {notifications && (
          <ActivityList activities={notifications}></ActivityList>
        )}
        {notifications.length === 0 && (
          <Typography variant="h5" textAlign="center">
            You have no notifications!
          </Typography>
        )}
      </Grid>
    </>
  );

  if (error) {
    return (
      <Typography variant="body2">
        Encounterd error {JSON.stringify(error, null, 2)}
      </Typography>
    );
  }

  if (!dataFetched) {
    return <LoadingScreen />;
  } else {
    return (
      <>
        <AppBar className={classes.appBar}>
          <Toolbar>
            <Typography
              variant="h6"
              noWrap
              component="div"
              sx={{ flexGrow: 1 }}
            >
              Activity Stream
            </Typography>

            <Button
              variant="outlined"
              color="inherit"
              disabled={notifications.length === 0}
              onClick={mergeNotificationsIntoFeed}
            >
              Merge Notifications into feed
            </Button>

            <IconButton
              size="large"
              aria-label={`show ${notifications.length} new notifications`}
              color="inherit"
              onClick={toggleNotification}
            >
              {notifications && (
                <Badge badgeContent={notifications.length} color="error">
                  <NotificationsIcon />
                </Badge>
              )}
            </IconButton>
          </Toolbar>
        </AppBar>

        <Grid container marginTop={"5rem"}>
          <Grid item xs>
            <Typography variant="h4" textAlign="center" marginBottom={"0.5rem"}>
              Activites fetched
            </Typography>
            {feed && <ActivityList activities={feed!}></ActivityList>}
            {feed?.length === 0 && (
              <Typography variant="h5" textAlign="center">
                You have no activity feed!
              </Typography>
            )}
          </Grid>
          {showNotifications && notificationContent}
        </Grid>
      </>
    );
  }
};
