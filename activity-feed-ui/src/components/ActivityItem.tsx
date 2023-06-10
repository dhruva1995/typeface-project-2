import { Card, CardContent, Typography } from "@mui/material";
import dayjs from "dayjs";
import relativeTime from "dayjs/plugin/relativeTime";
import React from "react";
import Activity from "../models/Activity";
import classes from "./ActivityItem.module.css";

dayjs.extend(relativeTime);

export type ActivityItemInput = {
  activity: Activity;
};

const ActivityItem: React.FC<ActivityItemInput> = ({ activity }) => {
  return (
    <Card className={classes.activityItem}>
      <CardContent>
        <Typography
          variant="body1"
          component="p"
          className={classes.activityMessage}
        >
          {activity.message}
        </Typography>

        <Typography
          className={classes.activityItemTimeSince}
          variant="subtitle2"
          color="text.secondary"
        >
          {dayjs(activity.when).fromNow()}
        </Typography>
      </CardContent>
    </Card>
  );
};

export default ActivityItem;
