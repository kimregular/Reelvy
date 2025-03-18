export default class User {
  private readonly _username: string;
  private readonly _nickname: string;
  private readonly _desc: string;
  private readonly _profileImageUrl: string;
  private readonly _backgroundImageUrl: string;


  constructor(username: string, nickname: string, desc: string, profileImageUrl: string, backgroundImageUrl: string) {
    this._username = username;
    this._nickname = nickname;
    this._desc = desc;
    this._profileImageUrl = profileImageUrl;
    this._backgroundImageUrl = backgroundImageUrl;
  }

  public static of(data:any): User {
    return new User(data.username,
      data.nickname,
      data.desc,
      data._profileImageUrl,
      data._backgroundImageUrl);
  }

  get username(): string {
    return this._username;
  }

  get nickname(): string {
    return this._nickname;
  }

  get desc(): string {
    return this._desc;
  }

  get profileImageUrl(): string {
    return this._profileImageUrl;
  }

  get backgroundImageUrl(): string {
    return this._backgroundImageUrl;
  }
}
