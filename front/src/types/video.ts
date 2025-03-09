import type User from "@/types/user.ts";

export default interface Video {
  id : number
  title : string;
  user : User;
  desc : string;
  videoPath : string;
  videoView : number;
}
