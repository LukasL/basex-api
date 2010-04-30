-------------------------------------------------------------------------------
-- |
-- Module      : Main
-- Copyright   : (C) Workgroup DBIS, University of Konstanz 2005-10
-- License     : ISC
--
-- Maintainer  : leo@woerteler.de
-- Stability   : experimental
-- Portability : portable
--
-- This example shows how BaseX commands can be performed via the Haskell API.
-- The execution time will be printed along with the result of the command.
--
-------------------------------------------------------------------------------

module Main where

import BaseXClient
import Network ( withSocketsDo )
import Data.Time.Clock ( getCurrentTime, diffUTCTime )
import Control.Applicative ( (<$>), (<*>), pure )

query :: String
query = "xquery 1 to 10"

main :: IO ()
main = withSocketsDo $ do
	-- start time
    start <- getCurrentTime
    -- connect to the server
    (Just session) <- connect "localhost" 1984 "admin" "admin"
    -- execute and print the query
    execute session query >>= putStrLn . either id content
    -- close the session
    close session
    -- print time difference
    (diffUTCTime <$> getCurrentTime <*> pure start) >>= print
    