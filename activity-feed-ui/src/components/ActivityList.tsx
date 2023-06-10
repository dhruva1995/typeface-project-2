import { List, ListItem } from "@mui/material";
import React from "react";
import Activity from "../models/Activity";
import ActivityItem from "./ActivityItem";
import classes from "./ActivityList.module.css";
export type ActivityListInput = {
  activities: Array<Activity>;
};

const ActivityList: React.FC<ActivityListInput> = ({ activities }) => {
  const content = activities.map((activity) => (
    <ListItem className={classes.activityListItem} key={activity.id}>
      <ActivityItem activity={activity}></ActivityItem>
    </ListItem>
  ));

  return <List className={classes.activityList}>{content}</List>;
};

export default ActivityList;
