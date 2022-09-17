import { createSlice, PayloadAction } from '@reduxjs/toolkit'
import OpenGameDto from './OpenGameDto'

interface AppData {
  openGames: OpenGameDto[],
}

const initialState: AppData = {
  openGames: [],
}

export const lobbySlice = createSlice({
  name: 'lobby',
  initialState,
  reducers: {
    setOpenGames: (state, action: PayloadAction<OpenGameDto[]>) => {
      state.openGames = action.payload
    },
  },
})

export const { setOpenGames } = lobbySlice.actions
export default lobbySlice.reducer