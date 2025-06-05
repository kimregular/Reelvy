export default interface Comment {
  id: number;
  content: string;
  author: string;
  createdAt: string;
  isAuthor: boolean;
  isVideoOwner: boolean;
}
