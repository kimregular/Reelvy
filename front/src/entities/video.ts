import type { UserResponseData } from '@/entities/user.ts'
import User from '@/entities/user.ts'
import router from '@/router'

export interface VideoResponseData {
  id: number
  title: string
  user: UserResponseData
  desc: string
  videoView: number
}

export default class Video {
  private readonly _id: number
  private readonly _title: string
  private readonly _user: User
  private readonly _desc: string
  private readonly _videoView: number

  constructor(id: number, title: string, user: User, desc: string, videoView: number) {
    this._id = id
    this._title = title
    this._user = user
    this._desc = desc
    this._videoView = videoView
    this.watch = this.watch.bind(this)
  }

  public static of(data: VideoResponseData, user: User): Video {
    return new Video(data.id, data.title, user, data.desc, data.videoView)
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

  get videoView(): number {
    return this._videoView
  }

  public async watch() {
    console.log('video watch func has been called!')
    await router.push({ name: 'WATCH', query: { videoId: this.id } })
  }
}
