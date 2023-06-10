import { Divider, Grid, Typography } from "@mui/material";
import React from "react";
import useWebSocket from "react-use-websocket";
import Activity from "../models/Activity";
import ActivityList from "./ActivityList";
import LoadingScreen from "./LoadingScreen";

export const ActivitiesScreen: React.FC<{ user: string }> = ({ user }) => {
  const [feed, setFeed] = React.useState<Activity[]>();
  const [notifications, setNotifications] = React.useState<Activity[]>([]);
  const [dataFetched, setDataFetched] = React.useState(false);
  const [error, setError] = React.useState<Error>();
  React.useEffect(() => {
    reset();
  }, []);

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
      //Will attempt to reconnect on all close events, such as server shutting down
      reconnectAttempts: 10,
      //attemptNumber will be 0 the first time it attempts to reconnect, so this equation results in a reconnect pattern of 1 second, 2 seconds, 4 seconds, 8 seconds, and then caps at 10 seconds until the maximum number of attempts is reached
      reconnectInterval: (attemptNumber) =>
        Math.min(Math.pow(2, attemptNumber) * 1000, 10000),
    }
  );

  React.useEffect(() => {
    //console.log(lastMessage);
    if (lastMessage && lastMessage.data) {
      const newActivity = JSON.parse(lastMessage.data) as Activity;
      setNotifications((oldNotifications) => [
        newActivity,
        ...oldNotifications,
      ]);
    }
  }, [lastMessage]);

  const reset = async () => {
    setFeed([]);
    setNotifications([]);
    setDataFetched(false);
    try {
      const response = await fetch(`http://localhost:8081/api/v1/feed/${user}`);
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
      <Grid container>
        <Grid item xs>
          <Typography variant="h2" textAlign="center">
            Activites fetched
          </Typography>
          <ActivityList activities={feed!}></ActivityList>
        </Grid>
        <Divider orientation="vertical" flexItem>
          <></>
        </Divider>
        <Grid item xs>
          <Typography variant="h2" textAlign="center">
            Notifications
          </Typography>
          <ActivityList activities={notifications}></ActivityList>
        </Grid>
      </Grid>
    );
  }
};

function fetchFeed(): Promise<Activity[]> {
  return Promise.resolve([
    {
      id: "648324b54dcd502460417384",
      message:
        "Welcome to workspace alpha!, Mcholay added you to the alpha workspace",
      when: 1686316213800,
    },
    {
      id: "648324b04dcd502460417383",
      message: "Welcome to workspace alpha",
      when: 1686316208801,
    },
    {
      id: "648324ab4dcd502460417382",
      message: "Welcome to workspace alpha",
      when: 1686316203801,
    },
    {
      id: "6483249c4dcd50246041737f",
      message: "Welcome to workspace alpha",
      when: 1686316188800,
    },
    {
      id: "648324974dcd50246041737e",
      message: "Welcome to workspace alpha",
      when: 1686316183801,
    },
    {
      id: "648324834dcd50246041737a",
      message: "Welcome to workspace alpha",
      when: 1686316163801,
    },
    {
      id: "648324794dcd502460417378",
      message: "Welcome to workspace alpha",
      when: 1686316153800,
    },
    {
      id: "6483246f4dcd502460417376",
      message: "Welcome to workspace alpha",
      when: 1686316143801,
    },
    {
      id: "648324564dcd502460417371",
      message: "Welcome to workspace alpha",
      when: 1686316118801,
    },
    {
      id: "648324514dcd502460417370",
      message: "Welcome to workspace alpha",
      when: 1686316113801,
    },
    {
      id: "6483244c4dcd50246041736f",
      message: "Welcome to workspace alpha",
      when: 1686316108801,
    },
    {
      id: "648324474dcd50246041736e",
      message: "Welcome to workspace alpha",
      when: 1686316103802,
    },
    {
      id: "648324424dcd50246041736d",
      message: "Welcome to workspace alpha",
      when: 1686316098801,
    },
    {
      id: "648324384dcd50246041736b",
      message: "Welcome to workspace alpha",
      when: 1686316088800,
    },
    {
      id: "6483242e4dcd502460417369",
      message: "Welcome to workspace alpha",
      when: 1686316078801,
    },
    {
      id: "648324294dcd502460417368",
      message: "Welcome to workspace alpha",
      when: 1686316073801,
    },
    {
      id: "6483240b4dcd502460417362",
      message: "Welcome to workspace alpha",
      when: 1686316043801,
    },
    {
      id: "648323fc4dcd50246041735f",
      message: "Welcome to workspace alpha",
      when: 1686316028801,
    },
    {
      id: "648323ed4dcd50246041735c",
      message: "Welcome to workspace alpha",
      when: 1686316013801,
    },
    {
      id: "648323de4dcd502460417359",
      message: "Welcome to workspace alpha",
      when: 1686315998800,
    },
    {
      id: "648323d94dcd502460417358",
      message: "Welcome to workspace alpha",
      when: 1686315993800,
    },
    {
      id: "648323cf4dcd502460417356",
      message: "Welcome to workspace alpha",
      when: 1686315983800,
    },
    {
      id: "648323bb4dcd502460417352",
      message: "Welcome to workspace alpha",
      when: 1686315963801,
    },
    {
      id: "648323b64dcd502460417351",
      message: "Welcome to workspace alpha",
      when: 1686315958801,
    },
    {
      id: "648323a24dcd50246041734d",
      message: "Welcome to workspace alpha",
      when: 1686315938802,
    },
    {
      id: "648323984dcd50246041734b",
      message: "Welcome to workspace alpha",
      when: 1686315928801,
    },
    {
      id: "648323934dcd50246041734a",
      message: "Welcome to workspace alpha",
      when: 1686315923800,
    },
    {
      id: "6483238e4dcd502460417349",
      message: "Welcome to workspace alpha",
      when: 1686315918802,
    },
    {
      id: "6483237f4dcd502460417346",
      message: "Welcome to workspace alpha",
      when: 1686315903800,
    },
    {
      id: "6483237a4dcd502460417345",
      message: "Welcome to workspace alpha",
      when: 1686315898800,
    },
    {
      id: "648323754dcd502460417344",
      message: "Welcome to workspace alpha",
      when: 1686315893800,
    },
    {
      id: "648323704dcd502460417343",
      message: "Welcome to workspace alpha",
      when: 1686315888801,
    },
    {
      id: "6483236c4dcd502460417342",
      message: "Welcome to workspace alpha",
      when: 1686315883813,
    },
    {
      id: "648318986050273d7244b856",
      message: "Welcome to workspace alpha",
      when: 1686313112379,
    },
    {
      id: "6483187f6050273d7244b851",
      message: "Welcome to workspace alpha",
      when: 1686313087379,
    },
    {
      id: "6483187a6050273d7244b850",
      message: "Welcome to workspace alpha",
      when: 1686313082379,
    },
    {
      id: "648318616050273d7244b84b",
      message: "Welcome to workspace alpha",
      when: 1686313057379,
    },
    {
      id: "6483185c6050273d7244b84a",
      message: "Welcome to workspace alpha",
      when: 1686313052379,
    },
    {
      id: "6483184d6050273d7244b847",
      message: "Welcome to workspace alpha",
      when: 1686313037380,
    },
    {
      id: "648318486050273d7244b846",
      message: "Welcome to workspace alpha",
      when: 1686313032379,
    },
    {
      id: "648318436050273d7244b845",
      message: "Welcome to workspace alpha",
      when: 1686313027379,
    },
    {
      id: "6483183e6050273d7244b844",
      message: "Welcome to workspace alpha",
      when: 1686313022379,
    },
    {
      id: "648318396050273d7244b843",
      message: "Welcome to workspace alpha",
      when: 1686313017380,
    },
    {
      id: "648318256050273d7244b83f",
      message: "Welcome to workspace alpha",
      when: 1686312997379,
    },
    {
      id: "6483181b6050273d7244b83d",
      message: "Welcome to workspace alpha",
      when: 1686312987379,
    },
    {
      id: "648318116050273d7244b83b",
      message: "Welcome to workspace alpha",
      when: 1686312977379,
    },
    {
      id: "648318076050273d7244b839",
      message: "Welcome to workspace alpha",
      when: 1686312967379,
    },
    {
      id: "648317fd6050273d7244b837",
      message: "Welcome to workspace alpha",
      when: 1686312957379,
    },
    {
      id: "648317ee6050273d7244b834",
      message: "Welcome to workspace alpha",
      when: 1686312942378,
    },
    {
      id: "648317df6050273d7244b831",
      message: "Welcome to workspace alpha",
      when: 1686312927379,
    },
    {
      id: "648317d56050273d7244b82f",
      message: "Welcome to workspace alpha",
      when: 1686312917379,
    },
    {
      id: "648317d06050273d7244b82e",
      message: "Welcome to workspace alpha",
      when: 1686312912378,
    },
    {
      id: "648317c66050273d7244b82c",
      message: "Welcome to workspace alpha",
      when: 1686312902378,
    },
    {
      id: "648317c16050273d7244b82b",
      message: "Welcome to workspace alpha",
      when: 1686312897378,
    },
    {
      id: "648317b76050273d7244b829",
      message: "Welcome to workspace alpha",
      when: 1686312887379,
    },
    {
      id: "648317a86050273d7244b826",
      message: "Welcome to workspace alpha",
      when: 1686312872378,
    },
    {
      id: "648317a36050273d7244b825",
      message: "Welcome to workspace alpha",
      when: 1686312867378,
    },
    {
      id: "648317996050273d7244b823",
      message: "Welcome to workspace alpha",
      when: 1686312857377,
    },
    {
      id: "6483178f6050273d7244b821",
      message: "Welcome to workspace alpha",
      when: 1686312847378,
    },
    {
      id: "648317856050273d7244b81f",
      message: "Welcome to workspace alpha",
      when: 1686312837378,
    },
    {
      id: "6483176c6050273d7244b81a",
      message: "Welcome to workspace alpha",
      when: 1686312812378,
    },
    {
      id: "648317626050273d7244b818",
      message: "Welcome to workspace alpha",
      when: 1686312802378,
    },
    {
      id: "648317586050273d7244b816",
      message: "Welcome to workspace alpha",
      when: 1686312792378,
    },
    {
      id: "648317536050273d7244b815",
      message: "Welcome to workspace alpha",
      when: 1686312787378,
    },
    {
      id: "6483174e6050273d7244b814",
      message: "Welcome to workspace alpha",
      when: 1686312782378,
    },
    {
      id: "648317496050273d7244b813",
      message: "Welcome to workspace alpha",
      when: 1686312777378,
    },
    {
      id: "648317446050273d7244b812",
      message: "Welcome to workspace alpha",
      when: 1686312772378,
    },
    {
      id: "6483173f6050273d7244b811",
      message: "Welcome to workspace alpha",
      when: 1686312767378,
    },
    {
      id: "6483173a6050273d7244b810",
      message: "Welcome to workspace alpha",
      when: 1686312762379,
    },
    {
      id: "6483172b6050273d7244b80d",
      message: "Welcome to workspace alpha",
      when: 1686312747379,
    },
    {
      id: "6483171c6050273d7244b80a",
      message: "Welcome to workspace alpha",
      when: 1686312732378,
    },
    {
      id: "648317036050273d7244b805",
      message: "Welcome to workspace alpha",
      when: 1686312707379,
    },
    {
      id: "648316fe6050273d7244b804",
      message: "Welcome to workspace alpha",
      when: 1686312702379,
    },
    {
      id: "648316f46050273d7244b802",
      message: "Welcome to workspace alpha",
      when: 1686312692379,
    },
    {
      id: "648316ef6050273d7244b801",
      message: "Welcome to workspace alpha",
      when: 1686312687379,
    },
    {
      id: "648316d16050273d7244b7fb",
      message: "Welcome to workspace alpha",
      when: 1686312657378,
    },
    {
      id: "648316b86050273d7244b7f6",
      message: "Welcome to workspace alpha",
      when: 1686312632378,
    },
    {
      id: "648316b36050273d7244b7f5",
      message: "Welcome to workspace alpha",
      when: 1686312627378,
    },
    {
      id: "648316ae6050273d7244b7f4",
      message: "Welcome to workspace alpha",
      when: 1686312622378,
    },
    {
      id: "648316866050273d7244b7ec",
      message: "Welcome to workspace alpha",
      when: 1686312582377,
    },
    {
      id: "6483167c6050273d7244b7ea",
      message: "Welcome to workspace alpha",
      when: 1686312572378,
    },
    {
      id: "648316726050273d7244b7e8",
      message: "Welcome to workspace alpha",
      when: 1686312562377,
    },
    {
      id: "6483166d6050273d7244b7e7",
      message: "Welcome to workspace alpha",
      when: 1686312557378,
    },
    {
      id: "648316686050273d7244b7e6",
      message: "Welcome to workspace alpha",
      when: 1686312552378,
    },
    {
      id: "6483165e6050273d7244b7e4",
      message: "Welcome to workspace alpha",
      when: 1686312542377,
    },
    {
      id: "648316456050273d7244b7df",
      message: "Welcome to workspace alpha",
      when: 1686312517377,
    },
    {
      id: "6483163b6050273d7244b7dd",
      message: "Welcome to workspace alpha",
      when: 1686312507377,
    },
    {
      id: "6483162c6050273d7244b7da",
      message: "Welcome to workspace alpha",
      when: 1686312492377,
    },
    {
      id: "648316226050273d7244b7d8",
      message: "Welcome to workspace alpha",
      when: 1686312482377,
    },
    {
      id: "6483161d6050273d7244b7d7",
      message: "Welcome to workspace alpha",
      when: 1686312477377,
    },
    {
      id: "648316186050273d7244b7d6",
      message: "Welcome to workspace alpha",
      when: 1686312472379,
    },
    {
      id: "648316136050273d7244b7d5",
      message: "Welcome to workspace alpha",
      when: 1686312467380,
    },
    {
      id: "6483160e6050273d7244b7d4",
      message: "Welcome to workspace alpha",
      when: 1686312462377,
    },
    {
      id: "648316096050273d7244b7d3",
      message: "Welcome to workspace alpha",
      when: 1686312457377,
    },
    {
      id: "648315fa6050273d7244b7d0",
      message: "Welcome to workspace alpha",
      when: 1686312442377,
    },
    {
      id: "648315f06050273d7244b7ce",
      message: "Welcome to workspace alpha",
      when: 1686312432376,
    },
    {
      id: "648315e66050273d7244b7cc",
      message: "Welcome to workspace alpha",
      when: 1686312422376,
    },
    {
      id: "648315dc6050273d7244b7ca",
      message: "Welcome to workspace alpha",
      when: 1686312412381,
    },
    {
      id: "648315d76050273d7244b7c9",
      message: "Welcome to workspace alpha",
      when: 1686312407375,
    },
    {
      id: "648315d26050273d7244b7c8",
      message: "Welcome to workspace alpha",
      when: 1686312402373,
    },
  ]);
}
