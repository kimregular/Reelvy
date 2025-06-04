import type { UserResponseData } from '@/entities/user.ts'
import User from '@/entities/user.ts'

export interface VideoResponseData {
  id: number
  title: string
  user: UserResponseData
  desc: string
  videoStatus: string
  videoView: number
}

export default class Video {
  private readonly _id: number
  private readonly _title: string
  private readonly _user: User
  private readonly _desc: string
  private readonly _videoStatus: string
  private readonly _videoView: number

  constructor(
    id: number,
    title: string,
    user: User,
    desc: string,
    videoStatus: string,
    videoView: number,
  ) {
    this._id = id
    this._title = title
    this._user = user
    this._desc = desc
    this._videoStatus = videoStatus
    this._videoView = videoView
  }

  public static of(data: VideoResponseData): Video {
    return new Video(
      data.id,
      data.title,
      User.of(data.user),
      data.desc,
      data.videoStatus,
      data.videoView,
    )
  }

  get id(): number {
    return this._id
  }

  get title(): string {
    return this._title
  }

  get user(): User {
    return this._user
  }

  get desc(): string {
    return this._desc
  }

  get status(): string {
    return this._videoStatus
  }

  get videoView(): number {
    return this._videoView
  }
}
